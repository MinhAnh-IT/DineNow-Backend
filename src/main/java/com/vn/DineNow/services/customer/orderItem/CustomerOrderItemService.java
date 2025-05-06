package com.vn.DineNow.services.customer.orderItem;

import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.OrderItem;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.payload.request.orderItem.OrderItemRequest;
import java.util.List;
import java.util.Optional;

public interface CustomerOrderItemService {
    OrderItem createOrderItem(Order order, OrderItemRequest orderItemRequest) throws Exception;
    long getTotalPrice(List<OrderItemRequest> requests) throws Exception;
    Optional<OrderItem> getOrderItemIfCustomerAteButNotReviewed(User customer, MenuItem menuItem);


}
