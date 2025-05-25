package com.vn.DineNow.payload.response.order;


import com.vn.DineNow.payload.request.orderItem.OrderItemSimpleResponse;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimple;
import lombok.Getter;
import lombok.Setter;



import java.util.List;

@Getter
@Setter
public class OrderResponseForPayment {
    Long id;
    Long totalPrice;
    RestaurantSimple restaurant;
    List<OrderItemSimpleResponse> menuItems;

}
