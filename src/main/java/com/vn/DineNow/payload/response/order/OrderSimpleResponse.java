package com.vn.DineNow.payload.response.order;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.payload.request.orderItem.OrderItemSimpleResponse;
import com.vn.DineNow.payload.response.reservation.ReservationSimpleResponse;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimple;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderSimpleResponse {
    Long id;
    Long totalPrice;
    OrderStatus status;
    RestaurantSimple restaurants;
    List<OrderItemSimpleResponse> menuItems;
    ReservationSimpleResponse reservationSimpleResponse;
}
