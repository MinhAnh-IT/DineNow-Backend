package com.vn.DineNow.payload.request.bankAccount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankAccountRequest {
    @NotBlank(message = "Account holder name must not be blank")
    @NotNull(message = "Account holder name is required")
    String accountHolderName;

    @NotBlank(message = "Account number must not be blank")
    @NotNull(message = "Account number is required")
    String accountNumber;

    @NotBlank(message = "Bank name must not be blank")
    @NotNull(message = "Bank name is required")
    String bankName;
}
