package com.vn.DineNow.payload.response.statistical.revenue;

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
public class RestaurantRevenueDetail {
    String restaurantName;
    Integer totalOrder;
    BigDecimal totalRevenue;
}
