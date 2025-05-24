package com.vn.DineNow.payload.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotBlank(message = "Email should not be blank")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Password should not be blank")
    String password;
}
