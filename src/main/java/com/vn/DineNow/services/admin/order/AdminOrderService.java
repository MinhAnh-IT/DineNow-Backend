package com.vn.DineNow.services.admin.order;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.order.OrderDetailResponse;
import com.vn.DineNow.payload.response.order.OrderResponse;

import java.util.List;

public interface AdminOrderService {
    List<OrderResponse> getAllOrdersByStatus(OrderStatus status, int page, int size) throws CustomException;
    OrderDetailResponse getOrderDetails(long orderID) throws CustomException;
    List<OrderResponse> getAllOrder(int page, int size) throws CustomException;
}
