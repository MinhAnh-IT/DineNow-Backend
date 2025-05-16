package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.BankAccount;
import com.vn.DineNow.payload.request.bankAccount.BankAccountRequest;
import com.vn.DineNow.payload.request.bankAccount.BankAccountUpdateRequest;
import com.vn.DineNow.payload.response.bankAccount.BankAccountResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    BankAccountResponse toResponse(BankAccount bankAccount);

    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    BankAccount toEntity(BankAccountRequest request);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity( BankAccountUpdateRequest request, @MappingTarget BankAccount bankAccount);
}
