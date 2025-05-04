package com.vn.DineNow.payload.response.user;

import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.SignWith;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UserDetailsResponse {
    private Long id;

    private String fullName;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean isVerified;

    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    private SignWith provider;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
