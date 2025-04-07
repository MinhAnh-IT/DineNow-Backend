package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findFirstByReservation(Reservation reservation);

}
