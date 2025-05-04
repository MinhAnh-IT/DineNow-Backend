package com.vn.DineNow.payload.response.user;

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
    boolean isVerified;
    boolean enabled;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
    String role;
}
