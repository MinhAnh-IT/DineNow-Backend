package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.Payment;
import com.vn.DineNow.entities.Reservation;
import com.vn.DineNow.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByOrderAndStatus(Order order, PaymentStatus status);

}
