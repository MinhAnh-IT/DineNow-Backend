package com.vn.DineNow.payload.request.auth;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class VerifyOTPRequest {
    @Email(message = "Email should be valid")
    private String email;
    @Length(message = "OTP should be 6 characters", min = 6, max = 6)
    private String otp;
}
