package com.vn.DineNow.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Role {
    OWNER("OWNER"),
    CUSTOMER("CUSTOMER"),
    ADMIN("ADMIN");

    String name;

}
