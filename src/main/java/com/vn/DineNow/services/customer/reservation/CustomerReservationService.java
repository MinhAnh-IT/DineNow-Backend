package com.vn.DineNow.services.customer.reservation;

import com.vn.DineNow.entities.Reservation;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.payload.request.reservation.ReservationRequest;

import java.util.Optional;

public interface CustomerReservationService {
    Reservation createReservation(ReservationRequest reservationRequest) throws Exception;
    long getTotalReservationByRestaurantId(long restaurantId) throws Exception;
    Optional<Reservation> getReservationByCustomerAndRestaurant(User customer, Restaurant restaurant) throws Exception;
}
