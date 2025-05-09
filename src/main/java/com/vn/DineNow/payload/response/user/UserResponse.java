package com.vn.DineNow.payload.response.user;

import com.vn.DineNow.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UserResponse {
    long id;
    String fullName;
    String email;
    String phone;
    @Enumerated(EnumType.STRING)
    Role role;
    boolean isVerified;
    boolean enabled;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
