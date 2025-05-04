package com.vn.DineNow.payload.request.user;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserCreateRequest {

    @NotNull(message = "Full name should not be null")
    @NotBlank(message = "Full name should not be blank")
    @Size(max = 255)
    private String fullName;

    @NotNull(message = "Email should not be null")
    @NotBlank(message = "Email should not be blank")
    @Email
    @Size(max = 255)
    private String email;


    @NotNull(message = "Password must not be null")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character and be at least 8 characters long"
    )
    private String password;

    @NotNull(message = "Phone number must not be null")
    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone must be 10 digits and start with 0")
    private String phone;



}
