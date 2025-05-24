package com.vn.DineNow.payload.request.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email should not be blank")
    String email;
}
