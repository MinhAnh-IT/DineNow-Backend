package com.vn.DineNow.payload.response.user;

import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.SignWith;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailsResponse {
    Long id;

    String fullName;

    String email;

    String phone;

    @Enumerated(EnumType.STRING)
    Role role;

    Boolean isVerified;

    Boolean enabled;

    @Enumerated(EnumType.STRING)
    SignWith provider;

    OffsetDateTime createdAt;

    OffsetDateTime updatedAt;
}
