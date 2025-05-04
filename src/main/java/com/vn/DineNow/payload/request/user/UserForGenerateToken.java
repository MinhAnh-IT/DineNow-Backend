package com.vn.DineNow.payload.request.user;

import com.vn.DineNow.enums.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserForGenerateToken {
    Long id;

    String email;

    Role role = Role.CUSTOMER;

    boolean enabled;
}
