package com.vn.DineNow.payload.response.reservation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationSimpleResponse {
    long numberOfPeople;
    long numberOfChild;
    OffsetDateTime reservationTime;
}
