package com.vn.DineNow.payload.response.statistical.profit;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTotalProfitDetail {
    String restaurantName;
    Integer totalOrders;
    Long totalGuests;
    BigDecimal feePerGuest;
    BigDecimal totalProfit;
}
