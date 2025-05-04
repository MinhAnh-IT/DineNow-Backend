package com.vn.DineNow.payload.response.order;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.payload.request.orderItem.OrderItemSimpleResponse;
import com.vn.DineNow.payload.response.reservation.ReservationDetailsResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    long id;
    OrderStatus status;
    long totalPrice;
    List<OrderItemSimpleResponse> menuItems;
    ReservationDetailsResponse reservation;
}
