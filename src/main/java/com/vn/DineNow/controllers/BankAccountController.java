package com.vn.DineNow.controllers;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.bankAccount.BankAccountRequest;
import com.vn.DineNow.payload.request.bankAccount.BankAccountUpdateRequest;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.security.CustomUserDetails;
import com.vn.DineNow.services.owner.bankAccount.BankAccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/owner")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BankAccountController {
    BankAccountService bankAccountService;

    @PostMapping("/bank-accounts")
    public ResponseEntity<?> createBankAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BankAccountRequest request) throws CustomException {
        var result = bankAccountService.createBankAccount(userDetails.getId(), request);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/bank-accounts")
    public ResponseEntity<?> updateBankAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BankAccountUpdateRequest request) throws CustomException {
        var result = bankAccountService.updateBankAccount(userDetails.getId(), request);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bank-accounts")
    public ResponseEntity<?> getBankAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {
        var result = bankAccountService.getBankAccount(userDetails.getId());
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

}
