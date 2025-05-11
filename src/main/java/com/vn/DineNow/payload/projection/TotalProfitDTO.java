package com.vn.DineNow.payload.projection;

import java.math.BigDecimal;

public interface TotalProfitDTO {
    String getRestaurantName();
    Integer getTotalOrders();
    BigDecimal getFeePerGuest();
    Long getTotalGuests();
    BigDecimal getTotalProfit();
}
