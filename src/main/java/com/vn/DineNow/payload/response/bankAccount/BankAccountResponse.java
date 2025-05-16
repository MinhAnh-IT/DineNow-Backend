package com.vn.DineNow.payload.response.bankAccount;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BankAccountResponse {
    String accountHolderName;

    String accountNumber;

    String bankName;

    OffsetDateTime createdAt;

    OffsetDateTime updatedAt;
}
