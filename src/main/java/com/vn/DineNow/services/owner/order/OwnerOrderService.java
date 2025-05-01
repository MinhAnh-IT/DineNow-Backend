package com.vn.DineNow.services.owner.order;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.order.OrderDetailResponse;

import java.util.List;
import java.util.Set;

public interface OwnerOrderService{
    boolean cancelOrder(Long orderId) throws Exception;
    boolean confirmedOrder(Long orderId) throws Exception;
    boolean completeOrder(Long orderId) throws Exception;

    boolean updateOrderStatus(long ownerId, long orderId, OrderStatus status) throws CustomException;
    List<?> getAllOrderByStatuses(long ownerId, long restaurantId, Set<OrderStatus> statuses) throws CustomException;
    OrderDetailResponse getOrderDetail(long orderId, long ownerId) throws CustomException;

}
