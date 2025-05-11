package com.vn.DineNow.services.owner.statistical;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.statistical.revenue.MonthlyRevenueResponse;
import com.vn.DineNow.payload.response.statistical.revenue.YearlyRevenueResponse;

import java.time.YearMonth;

public interface OwnerStatisticalService {
    MonthlyRevenueResponse getMonthlyRevenueInYear(int year, long ownerId, long restaurantId) throws CustomException;
    YearlyRevenueResponse getYearlyRevenue(long ownerId, long restaurantId) throws CustomException;
    MonthlyRevenueResponse getMonthlyRevenueInRange(YearMonth startDate, YearMonth endDate, long ownerId, long restaurantId) throws CustomException;
}
