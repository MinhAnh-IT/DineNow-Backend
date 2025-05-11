package com.vn.DineNow.services.admin.statistical;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.statistical.revenue.RestaurantMonthlyRevenueResponse;
import com.vn.DineNow.payload.response.statistical.revenue.RestaurantTotalRevenueResponse;

import java.time.YearMonth;

public interface AdminRevenueService {
    RestaurantMonthlyRevenueResponse getMonthlyRevenue(YearMonth yearMonth) throws CustomException;
    RestaurantTotalRevenueResponse getTotalRevenuePerRestaurantSinceJoined() throws CustomException;
    RestaurantTotalRevenueResponse getMonthlyRevenueInRange(YearMonth startMonth, YearMonth endMonth) throws CustomException;
}
