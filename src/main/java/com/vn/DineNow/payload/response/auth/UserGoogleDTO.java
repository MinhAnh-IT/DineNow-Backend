package com.vn.DineNow.payload.response.auth;

import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.SignWith;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGoogleDTO {

    Long id;
    String fullName;
    String email;

    @Builder.Default
    Role role = Role.CUSTOMER;

    @Builder.Default
    Boolean isVerified = true;

    @Builder.Default
    Boolean enabled = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    SignWith provider = SignWith.GOOGLE;

    @Builder.Default
    OffsetDateTime createdAt = OffsetDateTime.now();

    @Builder.Default
    OffsetDateTime updatedAt = OffsetDateTime.now();
}
