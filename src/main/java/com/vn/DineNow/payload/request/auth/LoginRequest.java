package com.vn.DineNow.payload.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LoginRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
