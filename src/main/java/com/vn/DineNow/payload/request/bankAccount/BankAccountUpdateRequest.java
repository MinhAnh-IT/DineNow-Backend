package com.vn.DineNow.payload.request.bankAccount;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankAccountUpdateRequest {
    String accountHolderName;
    String accountNumber;
    String bankName;
}
