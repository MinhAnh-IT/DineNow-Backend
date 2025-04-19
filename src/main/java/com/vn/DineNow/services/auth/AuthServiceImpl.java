package com.vn.DineNow.services.auth;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.payload.response.auth.UserGoogleDTO;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.SignWith;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.UserMapper;
import com.vn.DineNow.payload.request.auth.GoogleLoginRequest;
import com.vn.DineNow.payload.request.auth.LoginRequest;
import com.vn.DineNow.payload.request.auth.ResetPasswordRequest;
import com.vn.DineNow.payload.request.auth.VerifyOTPRequest;
import com.vn.DineNow.payload.request.common.EmailRequest;
import com.vn.DineNow.payload.response.auth.LoginResponse;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.services.google.IGoogleAuthService;
import com.vn.DineNow.services.email.IEmailService;
import com.vn.DineNow.services.redis.IRedisService;
import com.vn.DineNow.util.CookieUtils;
import com.vn.DineNow.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements IAuthService{
    final UserMapper userMapper;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;
    final IRedisService redisService;
    final IEmailService emailService;
    final IGoogleAuthService googleAuthService;


    @Value("${DineNow.key.refreshToken}")
    String keyRefreshToken;

    @Value("${DineNow.jwt.refresh.expiration}")
    long refreshTokenExpire;

    @Value("${DineNow.cookie.isSecure}")
    boolean isSecure;

    @Value("${DineNow.key.forgotPassword}")
    String keyForgotPassword;

    @Value("${DineNow.key.verifyAccount}")
    String keyVerifyAccount;

    @Value("${DineNow.key.reset-password}")
    String keyResetPassword;

    @Override
    public UserDTO register(UserDTO userDTO) throws CustomException {
        if (userRepository.existsByEmail(userDTO.getEmail(),SignWith.LOCAL)) {
            throw new CustomException(StatusCode.EXIST_EMAIL, userDTO.getEmail());
        }
        if (userRepository.existsByPhone(userDTO.getPhone())) {
            throw new CustomException(StatusCode.EXIST_PHONE, userDTO.getPhone());
        }
        userDTO.setEnabled(true);
        userDTO.setRole(Role.CUSTOMER);
        userDTO.setProvider(SignWith.LOCAL);
        var user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toDTO(user);
    }


    @Override
    public LoginResponse login(LoginRequest request, HttpServletResponse response) throws CustomException{
        if(!userRepository.existsByEmail(request.getEmail(), SignWith.LOCAL))
            throw new CustomException(StatusCode.NOT_FOUND, "username", request.getEmail());
        User user = userRepository.findByEmailAndProvider(request.getEmail(), SignWith.LOCAL);
        if (user.getIsVerified() == null){
            throw new CustomException(StatusCode.UNVERIFIED_ACCOUNT);
        }
        var isPasswordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isPasswordMatch) {
            throw new CustomException(StatusCode.LOGIN_FAILED);
        }
        String accessToken = jwtService.generateAccessToken(userMapper.toDTO(user));
        String refreshToken = jwtService.generateRefreshToken(userMapper.toDTO(user));

        // Check status of user
        boolean isEnabled = user.getEnabled();
        if (!isEnabled) {
            throw new CustomException(StatusCode.ACCOUNT_DISABLED);
        }
        // Store refresh token in Redis
        String refreshTokenKey = keyRefreshToken + user.getId();
        redisService.saveObject(refreshTokenKey, refreshToken, refreshTokenExpire, TimeUnit.DAYS);

        // Set refresh token in cookie
        CookieUtils.addRefreshTokenCookie(response, refreshToken, isSecure, (int) refreshTokenExpire);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) throws CustomException{
        long userId = jwtService.getUserIdFromJWT(refreshToken);
        UserDTO user = userMapper.toDTO(userRepository.findById(userId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "user", String.valueOf(userId))
        ));

        // Check status account
        if (!user.getEnabled()){
            throw new CustomException(StatusCode.ACCOUNT_DISABLED);
        }

        // Get refresh token from redis for validation
        String refreshTokenKey = keyRefreshToken + jwtService.getUserIdFromJWT(refreshToken);
        String redisRefreshToken = redisService.getObject(refreshTokenKey, String.class);

        // Validate the refresh token
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new CustomException(StatusCode.UNAUTHORIZED_TOKEN);
        }

        // regenerate access token
        String newAccessToken = jwtService.generateAccessToken(user);
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    @Override
    public boolean sendVerificationEmail(EmailRequest request) throws CustomException{
        User user = userRepository.findByEmailAndProvider(request.getEmail(), SignWith.LOCAL);
        if (user == null) {
            throw new CustomException(StatusCode.NOT_FOUND, "username", request.getEmail());
        }
        if (!user.getEnabled()){
            throw new CustomException(StatusCode.ACCOUNT_DISABLED);
        }

        // Generate OTP
        String otp = generateOTP();
        String redisKey = keyVerifyAccount + request.getEmail();
        redisService.saveObject(redisKey, otp, 5, TimeUnit.MINUTES);

        // Send OTP email
        try {
            String subject = "Xac thực tài khoản";
            String templateName = "verify-account";
            String to = request.getEmail();
            emailService.sendEmail(to, subject, templateName, otp);
        } catch (CustomException e) {
            throw new CustomException(StatusCode.EMAIL_ERROR, e);
        }
        return true;
    }

    @Override
    public boolean verifyOTPForgotPassword(VerifyOTPRequest request) throws CustomException{
        String redisKey = keyForgotPassword + request.getEmail();
        String redisOtp = redisService.getObject(redisKey, String.class);
        if (redisOtp == null) {
            throw new CustomException(StatusCode.INVALID_OTP);
        }
        if (redisOtp.equals(request.getOtp())) {
            // OTP is valid, proceed with password reset
            String keyReset = keyResetPassword + request.getEmail();
            redisService.saveObject(keyReset, true, 5, TimeUnit.MINUTES);
            redisService.deleteObject(redisKey);
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyOTPVerifyAccount(VerifyOTPRequest request) throws CustomException {
        String redisKey = keyVerifyAccount + request.getEmail();
        String redisOtp = redisService.getObject(redisKey, String.class);
        if (redisOtp == null) {
            throw new CustomException(StatusCode.INVALID_OTP);
        }
        if (redisOtp.equals(request.getOtp())) {
            // OTP is valid, proceed with account verification
            User user = userRepository.findByEmailAndProvider(request.getEmail(), SignWith.LOCAL);
            user.setIsVerified(true);
            userRepository.save(user);
            redisService.deleteObject(redisKey);
            return true;
        }
        return false;
    }

    @Override
    public boolean forgotPassword(EmailRequest request) throws CustomException{
        User user = userRepository.findByEmailAndProvider(request.getEmail(), SignWith.LOCAL);
        if (user == null) {
            throw new CustomException(StatusCode.NOT_FOUND, "username", request.getEmail());
        }
        if (!user.getEnabled()){
            throw new CustomException(StatusCode.ACCOUNT_DISABLED);
        }
        // Generate OTP
        String otp = generateOTP();
        String redisKey = keyForgotPassword + request.getEmail();
        redisService.saveObject(redisKey, otp, 5, TimeUnit.MINUTES);
        // Send OTP email
        try {
            String subject = "Xác thực quên mật khẩu";
            String templateName = "reset-password";
            String to = request.getEmail();
            emailService.sendEmail(to, subject, templateName, otp);
        } catch (CustomException e) {
            throw new CustomException(StatusCode.EMAIL_ERROR, e);
        }
        return true;
    }

    @Override
    public boolean resetPassword(ResetPasswordRequest request) throws CustomException{
        String redisKey = keyResetPassword + request.getEmail();
        Boolean isReset = redisService.getObject(redisKey, Boolean.class);
        if (isReset == null) {
            throw new CustomException(StatusCode.RESET_TOKEN_EXPIRED);
        }
        if (isReset) {
            User user = userRepository.findByEmailAndProvider(request.getEmail(), SignWith.LOCAL);
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            redisService.deleteObject(redisKey);
            return true;
        }
        return false;
    }

    @Override
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtils.getCookieValue(request, "refreshToken");
        if (refreshToken != null) {
            String redisKey = keyRefreshToken + jwtService.getUserIdFromJWT(refreshToken);
            redisService.deleteObject(redisKey);
            CookieUtils.deleteCookie(request, response, "refreshToken");
            return true;
        }
        return false;
    }

    @Override
    public LoginResponse LoginWithGoogle(GoogleLoginRequest request, HttpServletResponse response) throws CustomException {
        User user = null;

        try {
            if (request.getIdToken() == null || request.getIdToken().isBlank()) {
                throw new CustomException(StatusCode.INVALID_TOKEN);
            }

            var payload = googleAuthService.verifyToken(request.getIdToken());
            String email = payload.getEmail();
            user = userRepository.findByEmailAndProvider(email, SignWith.GOOGLE);

            if (user != null && !user.getEnabled()) {
                throw new CustomException(StatusCode.ACCOUNT_DISABLED);
            }

            if (user == null) {
                String name = (String) payload.get("name");
                UserGoogleDTO userDTO = UserGoogleDTO.builder()
                        .email(email)
                        .fullName(name)
                        .build();
                user = userMapper.toEntityFromGoogle(userDTO);
                user = userRepository.save(user);
            }

            String accessToken = jwtService.generateAccessToken(userMapper.toDTO(user));
            String refreshToken = jwtService.generateRefreshToken(userMapper.toDTO(user));

            String refreshTokenKey = keyRefreshToken + user.getId();
            redisService.saveObject(refreshTokenKey, refreshToken, refreshTokenExpire, TimeUnit.DAYS);

            CookieUtils.addRefreshTokenCookie(response, refreshToken, isSecure, (int) refreshTokenExpire);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .build();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Google login error: ", e);
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
}
