package com.vn.DineNow.constrants;

import com.vn.DineNow.enums.OrderStatus;
import java.util.Set;

public class OrderStatusConstants {
    public static final Set<OrderStatus> SUCCESSFUL_STATUSES = Set.of(OrderStatus.PAID, OrderStatus.COMPLETED);
}
