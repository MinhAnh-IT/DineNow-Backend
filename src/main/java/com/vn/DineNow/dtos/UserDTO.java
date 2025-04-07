package com.vn.DineNow.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vn.DineNow.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
public class UserDTO {
    private Long id;

    @NotNull
    @Size(max = 255)
    private String fullName;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;

    @JsonProperty("isVerified")
    private Boolean isVerified;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
