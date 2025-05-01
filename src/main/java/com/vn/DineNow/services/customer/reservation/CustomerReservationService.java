package com.vn.DineNow.services.customer.reservation;

import com.vn.DineNow.entities.Reservation;
import com.vn.DineNow.payload.request.Reservation.ReservationRequest;

public interface CustomerReservationService {
    Reservation createReservation(ReservationRequest reservationRequest) throws Exception;
}
