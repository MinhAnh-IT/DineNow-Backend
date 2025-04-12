package com.vn.DineNow.controllers;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.auth.GoogleLoginRequest;
import com.vn.DineNow.payload.request.auth.LoginRequest;
import com.vn.DineNow.payload.request.auth.ResetPasswordRequest;
import com.vn.DineNow.payload.request.auth.VerifyOTPRequest;
import com.vn.DineNow.payload.request.common.EmailRequest;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.auth.LoginResponse;
import com.vn.DineNow.services.auth.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserDTO>> register(@Valid @RequestBody UserDTO userDTO) throws CustomException {
        UserDTO registeredUser = authService.register(userDTO);
        APIResponse<UserDTO> response = APIResponse.<UserDTO>builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(registeredUser)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> loginLocal(@Valid @RequestBody LoginRequest request, HttpServletResponse httpServletResponse) throws CustomException {
        LoginResponse loginResponse = authService.login(request, httpServletResponse);
        APIResponse<LoginResponse> response = APIResponse.<LoginResponse>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(loginResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<APIResponse<LoginResponse>> refreshAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) throws CustomException{
        LoginResponse loginResponse = authService.refreshToken(refreshToken);
        APIResponse<LoginResponse> response = APIResponse.<LoginResponse>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(loginResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-verification-otp")
    public ResponseEntity<APIResponse<Boolean>> sendVerificationEmail(@Valid @RequestBody EmailRequest request) throws CustomException {
        boolean isSent = authService.sendVerificationEmail(request);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(isSent)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse<Boolean>> forgotPassword(@Valid @RequestBody EmailRequest request) throws CustomException {
        boolean isSent = authService.forgotPassword(request);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(isSent)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-reset-password-otp")
    public ResponseEntity<APIResponse<Boolean>> verifyOTPForgotPassword(@Valid @RequestBody VerifyOTPRequest request) throws CustomException {
        boolean isVerified = authService.verifyOTPForgotPassword(request);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(isVerified)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-account")
    public ResponseEntity<APIResponse<Boolean>> verifyAccount(@Valid @RequestBody VerifyOTPRequest request) throws CustomException {
        boolean isVerified = authService.verifyOTPVerifyAccount(request);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(isVerified)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<APIResponse<Boolean>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) throws CustomException {
        boolean isReset = authService.resetPassword(request);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(isReset)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<APIResponse<Boolean>> logout(HttpServletRequest request, HttpServletResponse response) {
        boolean isLoggedOut = authService.logout(request, response);
        APIResponse<Boolean> apiResponse = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(isLoggedOut)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/google-login")
    public ResponseEntity<APIResponse<LoginResponse>> loginWithGoogle(@RequestBody GoogleLoginRequest request, HttpServletResponse httpServletResponse) throws CustomException{
        LoginResponse loginResponse = authService.LoginWithGoogle(request, httpServletResponse);
        APIResponse<LoginResponse> response = APIResponse.<LoginResponse>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(loginResponse)
                .build();
        return ResponseEntity.ok(response);
    }
}
