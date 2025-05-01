package com.vn.DineNow.payload.response.order;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.payload.response.reservation.ReservationResponse;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderResponse {
    Long id;
    OrderStatus status;
    Long totalPrice;
    ReservationResponse reservation;
    int totalItems;
}
