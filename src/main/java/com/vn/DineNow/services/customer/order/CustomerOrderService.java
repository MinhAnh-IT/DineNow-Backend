package com.vn.DineNow.services.customer.order;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.Order.OrderRequest;
import com.vn.DineNow.payload.response.order.OrderResponseForPayment;
import com.vn.DineNow.payload.response.order.OrderSimpleResponse;
import java.util.List;
import java.util.Set;

public interface CustomerOrderService {
    OrderSimpleResponse createOrder(long ownerId, long restaurantId, OrderRequest orderRequest) throws CustomException;
    List<OrderSimpleResponse> getAllOrderByRestaurantAndCustomer(long userId, long restaurantId) throws CustomException;

    List<OrderSimpleResponse> getAllOrderByCustomer(long ownerId) throws CustomException;
    List<OrderSimpleResponse> getAllOrderByStatusPendingAndPaid(long ownerId, Set<OrderStatus> statuses) throws CustomException;
    boolean cancelOrder(long ownerId, Long orderId) throws CustomException;
    OrderResponseForPayment getOrderById(long customerId, long orderId) throws CustomException;
}
