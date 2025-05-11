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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RestaurantTotalRevenueResponse {
    BigDecimal totalRevenue;
    List<RestaurantRevenueDetail> restaurantRevenueDetails;
}
