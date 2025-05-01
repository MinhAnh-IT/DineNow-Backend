package com.vn.DineNow.services.customer.orderItem;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.OrderItem;
import com.vn.DineNow.payload.request.OrderItem.OrderItemRequest;

import java.util.List;

public interface CustomerOrderItemService {
    OrderItem createOrderItem(Order order, OrderItemRequest orderItemRequest) throws Exception;
    long getTotalPrice(List<OrderItemRequest> requests) throws Exception;
}
