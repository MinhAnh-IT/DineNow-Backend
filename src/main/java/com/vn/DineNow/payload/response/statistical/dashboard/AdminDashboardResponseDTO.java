package com.vn.DineNow.payload.response.statistical.dashboard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class AdminDashboardResponseDTO {
    Integer totalRestaurant;
    Integer totalUser;
    Integer totalOwner;
    BigDecimal totalRevenue;
    BigDecimal totalProfit;
    Integer totalOrderCompleted;
}
