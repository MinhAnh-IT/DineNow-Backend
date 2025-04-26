package com.vn.DineNow.services.common.auth;

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
import com.vn.DineNow.services.common.email.EmailService;
import com.vn.DineNow.services.common.cache.RedisService;
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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Service implementation for authentication-related operations including registration,
 * login (local and Google), refresh token, password management, and OTP verification.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    final UserMapper userMapper;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;
    final RedisService redisService;
    final EmailService emailService;
    final GoogleAuthService googleAuthService;

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

    /**
     * Registers a new user with local sign-up method.
     *
     * @param userDTO the user information to register
     * @return the registered user DTO
     * @throws CustomException if the email or phone number already exists
     */
    @Override
    public UserDTO register(UserDTO userDTO) throws CustomException {
        if (userRepository.existsByEmail(userDTO.getEmail(), SignWith.LOCAL)) {
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

    /**
     * Authenticates a user with email and password.
     *
     * @param request the login request containing email and password
     * @param response the HTTP response to set the refresh token cookie
     * @return a login response containing an access token
     * @throws CustomException if login fails or user account is disabled
     */
    @Override
    public LoginResponse login(LoginRequest request, HttpServletResponse response) throws CustomException {
        if (!userRepository.existsByEmail(request.getEmail(), SignWith.LOCAL)) {
            throw new CustomException(StatusCode.NOT_FOUND, "user", request.getEmail());
        }
        User user = userRepository.findByEmailAndProvider(request.getEmail(), SignWith.LOCAL);
        if (user.getIsVerified() == null) {
            throw new CustomException(StatusCode.UNVERIFIED_ACCOUNT);
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(StatusCode.LOGIN_FAILED);
        }
        if (!user.getEnabled()) {
            throw new CustomException(StatusCode.ACCOUNT_DISABLED);
        }

        String accessToken = jwtService.generateAccessToken(userMapper.toDTO(user));
        String refreshToken = jwtService.generateRefreshToken(userMapper.toDTO(user));
        String refreshTokenKey = keyRefreshToken + user.getId();

        redisService.saveObject(refreshTokenKey, refreshToken, refreshTokenExpire, TimeUnit.DAYS);
        CookieUtils.addRefreshTokenCookie(response, refreshToken, isSecure, (int) refreshTokenExpire);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param refreshToken the refresh token
     * @return a new login response with a new access token
     * @throws CustomException if the refresh token is invalid or expired
     */
    @Override
    public LoginResponse refreshToken(String refreshToken) throws CustomException {
        long userId = jwtService.getUserIdFromJWT(refreshToken);
        UserDTO user = userMapper.toDTO(userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "user", String.valueOf(userId))));

        if (!user.getEnabled()) {
            throw new CustomException(StatusCode.ACCOUNT_DISABLED);
        }

        String redisRefreshToken = redisService.getObject(keyRefreshToken + userId, String.class);
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new CustomException(StatusCode.UNAUTHORIZED_TOKEN);
        }

        String newAccessToken = jwtService.generateAccessToken(user);
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    /**
     * Sends a verification email with OTP for account verification.
     *
     * @param request the email request
     * @return true if the email was sent successfully
     * @throws CustomException if user not found or email sending fails
     */
    @Override
    public boolean sendVerificationEmail(EmailRequest request) throws CustomException {
        User user = userRepository.findByEmailAndProvider(request.getEmail(), SignWith.LOCAL);
        if (user == null) {
            throw new CustomException(StatusCode.NOT_FOUND, "user", request.getEmail());
        }
        if (!user.getEnabled()) {
            throw new CustomException(StatusCode.ACCOUNT_DISABLED);
        }

        String otp = generateOTP();
        redisService.saveObject(keyVerifyAccount + request.getEmail(), otp, 5, TimeUnit.MINUTES);

        try {
            emailService.sendEmail(request.getEmail(), "Xác thực tài khoản", "verify-account", otp);
        } catch (CustomException e) {
            throw new CustomException(StatusCode.EMAIL_ERROR, e);
        }
        return true;
    }

    /**
     * Verifies the OTP for forgot password flow.
     *
     * @param request the OTP verification request
     * @return true if verification successful
     * @throws CustomException if OTP is invalid
     */
    @Override
    public boolean verifyOTPForgotPassword(VerifyOTPRequest request) throws CustomException {
        String redisOtp = redisService.getObject(keyForgotPassword + request.getEmail(), String.class);
        if (redisOtp == null) {
            throw new CustomException(StatusCode.INVALID_OTP);
        }
        if (redisOtp.equals(request.getOtp())) {
            redisService.saveObject(keyResetPassword + request.getEmail(), true, 5, TimeUnit.MINUTES);
            redisService.deleteObject(keyForgotPassword + request.getEmail());
            return true;
        }
        return false;
    }

    /**
     * Verifies the OTP for account verification flow.
     *
     * @param request the OTP verification request
     * @return true if verification successful
     * @throws CustomException if OTP is invalid
     */
    @Override
    public boolean verifyOTPVerifyAccount(VerifyOTPRequest request) throws CustomException {
        String redisOtp = redisService.getObject(keyVerifyAccount + request.getEmail(), String.class);
        if (redisOtp == null) {
            throw new CustomException(StatusCode.INVALID_OTP);
        }
        if (redisOtp.equals(request.getOtp())) {
            User user = userRepository.findByEmailAndProvider(request.getEmail(), SignWith.LOCAL);
            user.setIsVerified(true);
            userRepository.save(user);
            redisService.deleteObject(keyVerifyAccount + request.getEmail());
            return true;
        }
        return false;
    }

    /**
     * Initiates the forgot password process by sending OTP.
     *
     * @param request the email request
     * @return true if OTP was sent successfully
     * @throws CustomException if user not found or email sending fails
     */
    @Override
    public boolean forgotPassword(EmailRequest request) throws CustomException {
        User user = userRepository.findByEmailAndProvider(request.getEmail(), SignWith.LOCAL);
        if (user == null) {
            throw new CustomException(StatusCode.NOT_FOUND, "user", request.getEmail());
        }
        if (!user.getEnabled()) {
            throw new CustomException(StatusCode.ACCOUNT_DISABLED);
        }

        String otp = generateOTP();
        redisService.saveObject(keyForgotPassword + request.getEmail(), otp, 5, TimeUnit.MINUTES);

        try {
            emailService.sendEmail(request.getEmail(), "Xác thực quên mật khẩu", "reset-password", otp);
        } catch (CustomException e) {
            throw new CustomException(StatusCode.EMAIL_ERROR, e);
        }
        return true;
    }

    /**
     * Resets user's password after OTP verification.
     *
     * @param request the reset password request
     * @return true if password reset successfully
     * @throws CustomException if reset token is invalid or expired
     */
    @Override
    public boolean resetPassword(ResetPasswordRequest request) throws CustomException {
        Boolean isReset = redisService.getObject(keyResetPassword + request.getEmail(), Boolean.class);
        if (isReset == null) {
            throw new CustomException(StatusCode.RESET_TOKEN_EXPIRED);
        }
        if (isReset) {
            User user = userRepository.findByEmailAndProvider(request.getEmail(), SignWith.LOCAL);
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            redisService.deleteObject(keyResetPassword + request.getEmail());
            return true;
        }
        return false;
    }

    /**
     * Logs out the user by deleting refresh token from Redis and clearing the cookie.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return true if logout successful
     */
    @Override
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtils.getCookieValue(request, "refreshToken");
        if (refreshToken != null) {
            redisService.deleteObject(keyRefreshToken + jwtService.getUserIdFromJWT(refreshToken));
            CookieUtils.deleteCookie(request, response, "refreshToken");
            return true;
        }
        return false;
    }

    /**
     * Authenticates a user via Google login.
     *
     * @param request the Google login request containing ID token
     * @param response the HTTP response to set refresh token cookie
     * @return a login response containing an access token
     * @throws CustomException if login fails
     */
    @Override
    public LoginResponse LoginWithGoogle(GoogleLoginRequest request, HttpServletResponse response) throws CustomException {
        try {
            if (request.getIdToken() == null || request.getIdToken().isBlank()) {
                throw new CustomException(StatusCode.INVALID_TOKEN);
            }
            var payload = googleAuthService.verifyToken(request.getIdToken());
            String email = payload.getEmail();
            User user = userRepository.findByEmailAndProvider(email, SignWith.GOOGLE);

            if (user != null && !user.getEnabled()) {
                throw new CustomException(StatusCode.ACCOUNT_DISABLED);
            }

            if (user == null) {
                String name = (String) payload.get("name");
                user = userMapper.toEntityFromGoogle(UserGoogleDTO.builder().email(email).fullName(name).build());
                user = userRepository.save(user);
            }

            String accessToken = jwtService.generateAccessToken(userMapper.toDTO(user));
            String refreshToken = jwtService.generateRefreshToken(userMapper.toDTO(user));

            redisService.saveObject(keyRefreshToken + user.getId(), refreshToken, refreshTokenExpire, TimeUnit.DAYS);
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

    /**
     * Generates a random 6-digit OTP code.
     *
     * @return the generated OTP code
     */
    private String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
}
