package com.vn.DineNow.controllers;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.auth.LoginRequest;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.auth.LoginResponse;
import com.vn.DineNow.services.auth.IAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserDTO>> register(@RequestBody UserDTO userDTO) throws CustomException {
        UserDTO registeredUser = authService.register(userDTO);
        APIResponse<UserDTO> response = APIResponse.<UserDTO>builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(registeredUser)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> loginLocal(@RequestBody LoginRequest request, HttpServletResponse httpServletResponse) throws CustomException {
        LoginResponse loginResponse = authService.login(request, httpServletResponse);
        APIResponse<LoginResponse> response = APIResponse.<LoginResponse>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(loginResponse)
                .build();
        return ResponseEntity.ok(response);
    }



}
