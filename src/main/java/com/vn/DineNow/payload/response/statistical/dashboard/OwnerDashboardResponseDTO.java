package com.vn.DineNow.payload.response.statistical.dashboard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OwnerDashboardResponseDTO {
    Integer totalRestaurant;
    Integer totalOrderCompleted;
    BigDecimal totalRevenue;
    Integer totalFoodCategory;
    Integer totalRestaurantReview;
    Integer totalMenuItemReview;
}
