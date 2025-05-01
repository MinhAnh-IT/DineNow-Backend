package com.vn.DineNow.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum SignWith {
    GOOGLE("GOOGLE"),
    LOCAL("LOCAL");

    String name;
}
