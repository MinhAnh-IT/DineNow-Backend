package com.vn.DineNow.services.owner.bankAccount;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.bankAccount.BankAccountRequest;
import com.vn.DineNow.payload.request.bankAccount.BankAccountUpdateRequest;
import com.vn.DineNow.payload.response.bankAccount.BankAccountResponse;

public interface BankAccountService {
    BankAccountResponse createBankAccount(Long ownerId, BankAccountRequest request) throws CustomException;
    BankAccountResponse updateBankAccount(Long id, BankAccountUpdateRequest request) throws CustomException;
    BankAccountResponse getBankAccount(Long ownerId) throws CustomException;
}
