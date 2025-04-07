package com.vn.DineNow.services.auth;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.auth.LoginRequest;
import com.vn.DineNow.payload.request.auth.ResetPasswordRequest;
import com.vn.DineNow.payload.request.auth.VerifyOTPRequest;
import com.vn.DineNow.payload.response.auth.LoginResponse;
import com.vn.DineNow.payload.request.common.EmailRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {
    UserDTO register(UserDTO userDTO) throws CustomException;
    LoginResponse login(LoginRequest request, HttpServletResponse response) throws  CustomException;
    LoginResponse refreshToken(String refreshToken);
    boolean sendVerificationEmail(EmailRequest request);
    boolean verifyOTP(VerifyOTPRequest request);
    boolean forgotPassword(EmailRequest request);
    boolean resetPassword(ResetPasswordRequest request);
    boolean logout(HttpServletRequest request);
}
