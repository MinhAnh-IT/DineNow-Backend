package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.Reservation;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Long countByRestaurantId(long restaurantId);
    Optional<Reservation> findTopByCustomerAndRestaurantAndReservationOrder_StatusAndReviewIsNull(
            User customer,
            Restaurant restaurant,
            OrderStatus status
    );


}
