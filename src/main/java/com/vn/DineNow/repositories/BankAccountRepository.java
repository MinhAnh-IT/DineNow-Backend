package com.vn.DineNow.repositories;


import com.vn.DineNow.entities.BankAccount;
import com.vn.DineNow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    boolean existsByUser(User owner);

    Optional<BankAccount> findByUser_Id(Long ownerId);
}
