package com.vn.DineNow.payload.request.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email should not be blank")
    private String email;
}
