package com.vn.DineNow.services.admin.order;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.OrderMapper;
import com.vn.DineNow.payload.response.order.OrderDetailResponse;
import com.vn.DineNow.payload.response.order.OrderResponse;
import com.vn.DineNow.repositories.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderServiceImpl implements AdminOrderService{
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    @Override
    public List<OrderResponse> getAllOrdersByStatus(OrderStatus status, int page, int size) throws CustomException {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        var orders = orderRepository.findAllByStatus(status, pageable);
        return orders.stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    public OrderDetailResponse getOrderDetails(long orderID) throws CustomException {
        var order = orderRepository.findById(orderID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "order", String.valueOf(orderID)));
        return orderMapper.toDetailDTO(order);
    }

    @Override
    public List<OrderResponse> getAllOrder(int page, int size) throws CustomException {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        var orders = orderRepository.findAll(pageable);

        return orders.stream()
                .map(orderMapper::toDTO)
                .toList();
    }
}
