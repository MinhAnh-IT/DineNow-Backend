package com.vn.DineNow.services.owner.bankAccount;

import com.vn.DineNow.entities.BankAccount;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.BankAccountMapper;
import com.vn.DineNow.payload.request.bankAccount.BankAccountRequest;
import com.vn.DineNow.payload.request.bankAccount.BankAccountUpdateRequest;
import com.vn.DineNow.payload.response.bankAccount.BankAccountResponse;
import com.vn.DineNow.repositories.BankAccountRepository;
import com.vn.DineNow.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BankAccountServiceImpl implements  BankAccountService{
    BankAccountRepository bankAccountRepository;
    UserRepository userRepository;
    BankAccountMapper bankAccountMapper;

    /**
     * Create a new bank account for the owner
     *
     * @param request the request containing bank account details
     * @return the created bank account response
     * @throws CustomException if the owner is not found or already has a bank account
     */
    @Override
    public BankAccountResponse createBankAccount(Long ownerId, BankAccountRequest request) throws CustomException {
        var owner = userRepository.findById(ownerId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId)));
        if(!owner.getRole().equals(Role.OWNER)){
            throw new CustomException(StatusCode.FORBIDDEN);
        }
        if(bankAccountRepository.existsByUser(owner)){
            throw new CustomException(StatusCode.INVALID_ACTION, "Bank account already exists");
        }
        var bankAccount = bankAccountMapper.toEntity(request);
        bankAccount.setUser(owner);

        bankAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toResponse(bankAccount);
    }

    /**
     * Update the bank account details for the owner
     *
     * @param request the request containing updated bank account details
     * @return the updated bank account response
     * @throws CustomException if the owner is not found or the bank account does not exist
     */
    @Override
    public BankAccountResponse updateBankAccount(Long ownerId, BankAccountUpdateRequest request) throws CustomException {
        var bankAccount = bankAccountRepository.findByUser_Id(ownerId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "bank account", String.valueOf(ownerId)));

        bankAccountMapper.updateEntity(request, bankAccount);
        bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toResponse(bankAccount);
    }

    /**
     * Get the bank account details for the owner
     *
     * @param ownerId the ID of the owner
     * @return the bank account response
     * @throws CustomException if the bank account does not exist
     */
    @Override
    public BankAccountResponse getBankAccount(Long ownerId) throws CustomException {
        var bankAccount = bankAccountRepository.findByUser_Id(ownerId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "bank account", String.format("owner id = %s", ownerId)));
        return bankAccountMapper.toResponse(bankAccount);
    }
}
