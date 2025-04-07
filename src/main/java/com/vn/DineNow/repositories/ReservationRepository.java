package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.DiningTable;
import com.vn.DineNow.entities.Reservation;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findFirstByCustomer(User user);

    Reservation findFirstByRestaurant(Restaurant restaurant);

    Reservation findFirstByTable(DiningTable diningTable);

}
