package com.vn.DineNow.dtos;

import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.SignWith;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
public class UserGoogleDTO {

    private Long id;
    private String fullName;
    private String email;

    @Builder.Default
    private Role role = Role.CUSTOMER;

    @Builder.Default
    private Boolean isVerified = true;

    @Builder.Default
    private Boolean enabled = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private SignWith provider = SignWith.GOOGLE;

    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Builder.Default
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}
