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
public class RestaurantMonthlyProfitDetail {
    String restaurantName;
    Integer month;
    Integer year;
    Long totalGuests;
    BigDecimal feePerGuest;
    Integer totalOrders;
    BigDecimal profit;
}
