package com.vn.DineNow.services.auth;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.SignWith;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.UserMapper;
import com.vn.DineNow.payload.request.auth.LoginRequest;
import com.vn.DineNow.payload.request.auth.ResetPasswordRequest;
import com.vn.DineNow.payload.request.auth.VerifyOTPRequest;
import com.vn.DineNow.payload.request.common.EmailRequest;
import com.vn.DineNow.payload.response.auth.LoginResponse;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.services.Redis.IRedisService;
import com.vn.DineNow.util.CookieUtils;
import com.vn.DineNow.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService{
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final IRedisService redisService;
    private CookieUtils cookieUtils;

    @Value("${DineNow.key.refreshToken}")
    private String keyRefreshToken;

    @Value("${DineNow.jwt.refresh.expiration}")
    private long refreshTokenExpire;

    @Value("${DineNow.cookie.isSecure}")
    private boolean isSecure;

    @Override
    public UserDTO register(UserDTO userDTO) throws CustomException {
        if (userRepository.existsByEmail(userDTO.getEmail(),SignWith.LOCAL)) {
            throw new CustomException(StatusCode.EXIST_EMAIL, userDTO.getEmail());
        }
        if (userRepository.existsByPhone(userDTO.getPhone())) {
            throw new CustomException(StatusCode.EXIST_PHONE, userDTO.getPhone());
        }
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
        var isPasswordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isPasswordMatch) {
            throw new CustomException(StatusCode.INVALID_PASSWORD);
        }
        String accessToken = jwtService.generateAccessToken(userMapper.toDTO(user));
        String refreshToken = jwtService.generateRefreshToken(userMapper.toDTO(user));

        // Store refresh token in Redis
        String refreshTokenKey = keyRefreshToken + "_" + user.getId();
        redisService.saveObject(refreshTokenKey, refreshToken, refreshTokenExpire, TimeUnit.DAYS);

        // Set refresh token in cookie
        CookieUtils.addRefreshTokenCookie(response, refreshToken, isSecure);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public boolean sendVerificationEmail(EmailRequest request) {
        return false;
    }

    @Override
    public boolean verifyOTP(VerifyOTPRequest request) {
        return false;
    }

    @Override
    public boolean forgotPassword(EmailRequest request) {
        return false;
    }

    @Override
    public boolean resetPassword(ResetPasswordRequest request) {
        return false;
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        return false;
    }
}
