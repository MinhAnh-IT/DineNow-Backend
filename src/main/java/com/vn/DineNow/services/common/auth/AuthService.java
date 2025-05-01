package com.vn.DineNow.services.common.auth;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.auth.GoogleLoginRequest;
import com.vn.DineNow.payload.request.auth.LoginRequest;
import com.vn.DineNow.payload.request.auth.ResetPasswordRequest;
import com.vn.DineNow.payload.request.auth.VerifyOTPRequest;
import com.vn.DineNow.payload.request.user.UserCreateRequest;
import com.vn.DineNow.payload.response.auth.LoginResponse;
import com.vn.DineNow.payload.request.common.EmailRequest;
import com.vn.DineNow.payload.response.user.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserResponse register(UserCreateRequest userDTO) throws CustomException;
    LoginResponse login(LoginRequest request, HttpServletResponse response) throws  CustomException;
    LoginResponse refreshToken(String refreshToken) throws CustomException;
    boolean sendVerificationEmail(EmailRequest request)throws CustomException;
    boolean verifyOTPForgotPassword(VerifyOTPRequest request) throws CustomException;
    boolean verifyOTPVerifyAccount(VerifyOTPRequest request) throws CustomException;
    boolean forgotPassword(EmailRequest request) throws CustomException;
    boolean resetPassword(ResetPasswordRequest request) throws CustomException;
    boolean logout(HttpServletRequest request, HttpServletResponse response);
    LoginResponse LoginWithGoogle(GoogleLoginRequest request, HttpServletResponse response) throws CustomException;
}
