package com.vn.DineNow.services.admin.statistical;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.statistical.profit.RestaurantMonthProfitResponse;
import com.vn.DineNow.payload.response.statistical.profit.RestaurantTotalProfitResponse;

import java.time.YearMonth;

public interface AdminProfitService {
    RestaurantMonthProfitResponse getMonthlyProfitsOfAllRestaurants(YearMonth yearMonth);
    RestaurantTotalProfitResponse getTotalProfitsPerRestaurant();
    RestaurantTotalProfitResponse getTotalProfitsPerRestaurantInRange(YearMonth startMonth, YearMonth endMonth) throws CustomException;
}
