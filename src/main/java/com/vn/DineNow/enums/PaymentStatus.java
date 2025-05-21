package com.vn.DineNow.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PaymentStatus {
    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED");
    String status;
}
