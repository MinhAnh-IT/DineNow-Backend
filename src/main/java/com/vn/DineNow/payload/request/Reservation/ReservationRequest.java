package com.vn.DineNow.payload.request.Reservation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationRequest {
    Long restaurantId;
    Long customerId;
    OffsetDateTime reservationTime;
    Integer numberOfPeople;
    String numberPhone;
    Integer numberOfChild;
}
