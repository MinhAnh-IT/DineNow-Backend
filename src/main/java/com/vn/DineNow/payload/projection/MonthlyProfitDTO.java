package com.vn.DineNow.payload.projection;

import java.math.BigDecimal;

public interface MonthlyProfitDTO {
    String getRestaurantName();

    Integer getMonth();

    Integer getTotalOrders();

    BigDecimal getTotalProfit();

    BigDecimal getFeePerGuest();

    Long getTotalGuests();

    Integer getYear();
}
