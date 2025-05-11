package com.vn.DineNow.payload.response.statistical.revenue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantMonthlyRevenueResponse {
    BigDecimal totalRevenue;
    List<RestaurantMonthlyRevenueDetail> restaurantMonthlyDetails;
}
