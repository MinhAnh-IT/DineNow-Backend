package com.vn.DineNow.payload.request.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPasswordRequest {
    private String otp;
    private String newPassword;
}
