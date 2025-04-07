package com.vn.DineNow.enums;

import lombok.Getter;

@Getter
public enum Role {
    OWNER("OWNER"),
    CUSTOMER("CUSTOMER"),
    ADMIN("ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
