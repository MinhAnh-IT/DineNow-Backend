package com.vn.DineNow.payload.request.auth;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyOTPRequest {
    @Email(message = "Email should be valid")
    String email;
    @Length(message = "OTP should be 6 characters", min = 6, max = 6)
    String otp;
}
