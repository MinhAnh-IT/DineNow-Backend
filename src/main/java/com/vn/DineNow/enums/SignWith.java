package com.vn.DineNow.enums;

import lombok.Getter;

@Getter
public enum SignWith {
    GOOGLE("GOOGLE"),
    LOCAL("LOCAL");

    private final String name;

    SignWith(String name) {
        this.name = name;
    }

}
