package com.vn.DineNow.payload.projection;

import java.math.BigDecimal;

public interface MonthlyRevenueDTO {
    String getRestaurantName();
    Integer getMonth();
    Integer getTotalOrders();
    BigDecimal getTotalRevenue();
    Integer getYear();
}
