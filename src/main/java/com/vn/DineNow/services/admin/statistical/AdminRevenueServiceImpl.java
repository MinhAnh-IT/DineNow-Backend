package com.vn.DineNow.services.admin.statistical;

import com.vn.DineNow.constrants.OrderStatusConstants;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.projection.MonthlyRevenueDTO;
import com.vn.DineNow.payload.projection.TotalRevenueDTO;
import com.vn.DineNow.payload.response.statistical.revenue.RestaurantMonthlyRevenueDetail;
import com.vn.DineNow.payload.response.statistical.revenue.RestaurantMonthlyRevenueResponse;
import com.vn.DineNow.payload.response.statistical.revenue.RestaurantTotalRevenueResponse;
import com.vn.DineNow.payload.response.statistical.revenue.RestaurantRevenueDetail;
import com.vn.DineNow.repositories.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminRevenueServiceImpl implements AdminRevenueService {

    OrderRepository orderRepository;

    /**
     * Retrieves the revenue of all restaurants for a specific month and year.
     * If no month is provided, the current month is used by default.
     *
     * @param yearMonth The year and month to retrieve revenue data for.
     * @return A response containing total revenue and per-restaurant revenue details.
     * @throws CustomException If any error occurs during processing.
     */
    @Override
    public RestaurantMonthlyRevenueResponse getMonthlyRevenue(YearMonth yearMonth) throws CustomException {
        if (yearMonth == null) {
            yearMonth = YearMonth.now();
        }

        var revenues = orderRepository.getAllRestaurantMonthlyRevenue(
                yearMonth.getMonthValue(), yearMonth.getYear(), OrderStatusConstants.SUCCESSFUL_STATUSES
        );

        List<RestaurantMonthlyRevenueDetail> monthlyDetails = revenues.stream()
                .map(revenue -> RestaurantMonthlyRevenueDetail.builder()
                        .restaurantName(revenue.getRestaurantName())
                        .month(revenue.getMonth())
                        .year(revenue.getYear())
                        .totalOrder(revenue.getTotalOrders())
                        .totalRevenue(revenue.getTotalRevenue())
                        .build())
                .toList();

        BigDecimal totalRevenue = revenues.stream()
                .map(MonthlyRevenueDTO::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RestaurantMonthlyRevenueResponse.builder()
                .totalRevenue(totalRevenue)
                .restaurantMonthlyDetails(monthlyDetails)
                .build();
    }

    /**
     * Converts a list of TotalRevenueDTO to a response containing per-restaurant revenue
     * and the aggregated total revenue across all restaurants.
     *
     * @param revenues The list of revenue data per restaurant.
     * @return A response containing total revenue and detailed breakdown.
     */
    private RestaurantTotalRevenueResponse convertTotalRevenueResponse(List<TotalRevenueDTO> revenues) {
        List<RestaurantRevenueDetail> details = revenues.stream()
                .map(dto -> RestaurantRevenueDetail.builder()
                        .restaurantName(dto.getRestaurantName())
                        .totalOrder(dto.getTotalOrders())
                        .totalRevenue(dto.getTotalRevenue())
                        .build())
                .toList();

        BigDecimal totalRevenue = details.stream()
                .map(RestaurantRevenueDetail::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RestaurantTotalRevenueResponse.builder()
                .totalRevenue(totalRevenue)
                .restaurantRevenueDetails(details)
                .build();
    }

    /**
     * Retrieves total revenue and order count for each restaurant since they joined.
     *
     * @return A response containing the total and per-restaurant revenue details.
     * @throws CustomException If any error occurs during processing.
     */
    @Override
    public RestaurantTotalRevenueResponse getTotalRevenuePerRestaurantSinceJoined() throws CustomException {
        var revenues = orderRepository.getTotalRevenuePerRestaurantSinceJoined(OrderStatusConstants.SUCCESSFUL_STATUSES);
        return convertTotalRevenueResponse(revenues);
    }

    /**
     * Retrieves the total revenue for all restaurants within a specified month range.
     * For example: from 2025-01 to 2025-04 will include all orders from January to April 2025.
     *
     * @param startMonth The start YearMonth (inclusive).
     * @param endMonth   The end YearMonth (inclusive).
     * @return A response containing total revenue and per-restaurant breakdown.
     * @throws CustomException If the date range is invalid or any other error occurs.
     */
    @Override
    public RestaurantTotalRevenueResponse getMonthlyRevenueInRange(YearMonth startMonth, YearMonth endMonth) throws CustomException {
        if (startMonth == null || endMonth == null || startMonth.isAfter(endMonth)) {
            throw new CustomException(StatusCode.INVALID_DATE_RANGE);
        }

        OffsetDateTime start = startMonth.atDay(1).atStartOfDay().atOffset(ZoneOffset.of("+07:00"));
        OffsetDateTime end = endMonth.atEndOfMonth().atTime(LocalTime.MAX).atOffset(ZoneOffset.of("+07:00"));

        var revenues = orderRepository.getMonthlyRevenueOfAllRestaurantsInRange(
                start, end, OrderStatusConstants.SUCCESSFUL_STATUSES
        );

        return convertTotalRevenueResponse(revenues);
    }
}
