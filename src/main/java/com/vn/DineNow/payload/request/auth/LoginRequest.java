package com.vn.DineNow.payload.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
public class LoginRequest {
    @NotBlank(message = "Email should not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password should not be blank")
    private String password;
}
