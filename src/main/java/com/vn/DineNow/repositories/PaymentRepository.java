package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.Payment;
import com.vn.DineNow.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findFirstByReservation(Reservation reservation);

}
