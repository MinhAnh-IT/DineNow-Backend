package com.vn.DineNow.services.admin.statistical;

import com.vn.DineNow.constrants.OrderStatusConstants;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.projection.MonthlyProfitDTO;
import com.vn.DineNow.payload.projection.TotalProfitDTO;
import com.vn.DineNow.payload.response.statistical.profit.RestaurantMonthProfitResponse;
import com.vn.DineNow.payload.response.statistical.profit.RestaurantMonthlyProfitDetail;
import com.vn.DineNow.payload.response.statistical.profit.RestaurantTotalProfitDetail;
import com.vn.DineNow.payload.response.statistical.profit.RestaurantTotalProfitResponse;
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
public class AdminProfitServiceImpl implements AdminProfitService{
    OrderRepository orderRepository;

    /**
     * Retrieves the monthly profits for all restaurants for a given month.
     *
     * @param yearMonth The year and month for which to retrieve the profits. If null, the current month is used.
     * @return A RestaurantMonthProfitResponse containing the total profit and details for each restaurant.
     */
    @Override
    public RestaurantMonthProfitResponse getMonthlyProfitsOfAllRestaurants(YearMonth yearMonth) {
        // If yearMonth is null, use the current month
        if (yearMonth == null) {
            yearMonth = YearMonth.now();
        }

        // Get the monthly profit details for all restaurants
        var profitDetails = orderRepository.getMonthlyProfitsOfAllRestaurants(
                yearMonth.getMonthValue(),
                yearMonth.getYear(),
                OrderStatusConstants.SUCCESSFUL_STATUSES
        );

        // Calculate the total profit
        var totalProfit = profitDetails.stream()
                .map(MonthlyProfitDTO::getTotalProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Map the profit details to the response DTO
        YearMonth finalYearMonth = yearMonth;
        List<RestaurantMonthlyProfitDetail> profitDetailList = profitDetails.stream()
                .map(dto -> RestaurantMonthlyProfitDetail.builder()
                        .restaurantName(dto.getRestaurantName())
                        .month(dto.getMonth() != null ? dto.getMonth() : finalYearMonth.getMonthValue())
                        .year(dto.getYear() != null ? dto.getYear() : finalYearMonth.getYear())
                        .feePerGuest(dto.getFeePerGuest())
                        .totalGuests(dto.getTotalGuests())
                        .totalOrders(dto.getTotalOrders())
                        .profit(dto.getTotalProfit())
                        .build())
                .toList();

        return RestaurantMonthProfitResponse.builder()
                .totalProfit(totalProfit)
                .monthlyProfitDetails(profitDetailList)
                .build();
    }


    @Override
    public RestaurantTotalProfitResponse getTotalProfitsPerRestaurant() {
        // Get the total profits for all restaurants
        var profitDetails = orderRepository.getTotalProfitsPerRestaurant(OrderStatusConstants.SUCCESSFUL_STATUSES);
        return convertTotalProfitResponse(profitDetails);
    }


    /**
     * Converts a list of TotalProfitDTO to a response containing per-restaurant profit
     * and the aggregated total profit across all restaurants.
     *
     * @param profits The list of profit data per restaurant.
     * @return A response containing total profit and detailed breakdown.
     */
    private RestaurantTotalProfitResponse convertTotalProfitResponse(List<TotalProfitDTO> profits) {
        // Map the profit details to the response DTO
        List<RestaurantTotalProfitDetail> profitDetailList = profits.stream()
                .map(dto -> RestaurantTotalProfitDetail.builder()
                        .restaurantName(dto.getRestaurantName())
                        .totalGuests(dto.getTotalGuests())
                        .feePerGuest(dto.getFeePerGuest())
                        .totalOrders(dto.getTotalOrders())
                        .totalProfit(dto.getTotalProfit())
                        .build())
                .toList();

        // Calculate the total profit
        var totalProfit = profits.stream()
                .map(TotalProfitDTO::getTotalProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RestaurantTotalProfitResponse.builder()
                .totalProfit(totalProfit)
                .totalProfitDetails(profitDetailList)
                .build();
    }

    /**
     * Retrieves the total profits for all restaurants within a specified date range.
     *
     * @param startMonth The start month of the range.
     * @param endMonth   The end month of the range.
     * @return A RestaurantTotalProfitResponse containing the total profit and details for each restaurant.
     * @throws CustomException If the date range is invalid or if any error occurs during processing.
     */
    @Override
    public RestaurantTotalProfitResponse getTotalProfitsPerRestaurantInRange(YearMonth startMonth, YearMonth endMonth) throws CustomException {
        // validate the input months
        if (startMonth == null || endMonth == null || startMonth.isAfter(endMonth)) {
            throw new CustomException(StatusCode.INVALID_DATE_RANGE);
        }

        // convert YearMonth to OffsetDateTime with timezone Asia/Ho_Chi_Minh
        OffsetDateTime start = startMonth.atDay(1).atStartOfDay().atOffset(ZoneOffset.of("+07:00"));
        OffsetDateTime end = endMonth.atEndOfMonth().atTime(LocalTime.MAX).atOffset(ZoneOffset.of("+07:00"));


        var profitDetails = orderRepository.getTotalProfitsPerRestaurantInRange(
                start,
                end,
                OrderStatusConstants.SUCCESSFUL_STATUSES
        );
        return convertTotalProfitResponse(profitDetails);
    }
}
