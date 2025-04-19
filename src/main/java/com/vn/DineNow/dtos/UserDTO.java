package com.vn.DineNow.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.SignWith;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@Builder
public class UserDTO {
    private Long id;

    @NotBlank(message = "Full name should not be blank")
    @Size(max = 255)
    private String fullName;

    @NotBlank(message = "Email should not be blank")
    @Email
    @Size(max = 255)
    private String email;

    @Builder.Default
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private SignWith provider = SignWith.LOCAL;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character and be at least 8 characters long"
    )
    private String password;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;

    @Builder.Default
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;

    @JsonProperty("isVerified")
    private Boolean isVerified;

    @Builder.Default
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean enabled = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime updatedAt;

}
