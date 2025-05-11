package com.vn.DineNow.payload.projection;

import java.math.BigDecimal;

public interface TotalRevenueDTO {
    String getRestaurantName();
    Integer getTotalOrders();
    BigDecimal getTotalRevenue();
}
