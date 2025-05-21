package com.vn.DineNow.services.owner.order;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.Order.RejectOrderRequest;
import com.vn.DineNow.payload.response.order.OrderDetailResponse;

import java.util.List;
import java.util.Set;

public interface OwnerOrderService{
    boolean updateOrderStatus(long ownerId, long orderId, OrderStatus status, RejectOrderRequest reason) throws CustomException;
    List<?> getAllOrderByStatuses(long ownerId, long restaurantId, Set<OrderStatus> statuses) throws CustomException;
    OrderDetailResponse getOrderDetail(long orderId, long ownerId) throws CustomException;
    void updateOrderStatusFromCallBackPayment(long orderId, OrderStatus status) throws CustomException;
}
