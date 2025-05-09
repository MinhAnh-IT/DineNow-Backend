package com.vn.DineNow.services.owner.statistical;

import com.vn.DineNow.constrants.OrderStatusConstants;
import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.projection.MonthlyRevenueDTO;
import com.vn.DineNow.payload.response.statistical.MonthlyRevenueDetail;
import com.vn.DineNow.payload.response.statistical.MonthlyRevenueResponse;
import com.vn.DineNow.payload.response.statistical.YearlyRevenueDetail;
import com.vn.DineNow.payload.response.statistical.YearlyRevenueResponse;
import com.vn.DineNow.repositories.OrderRepository;
import com.vn.DineNow.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnerStatisticalServiceImpl implements OwnerStatisticalService{
    OrderRepository orderRepository;
    UserRepository userRepository;


    /**
     * Retrieves the monthly revenue for a specific year and restaurant.
     *
     * @param year         The year for which to retrieve the revenue.
     * @param ownerId      The ID of the restaurant owner.
     * @param restaurantId The ID of the restaurant.
     * @return A MonthlyRevenueResponse containing the total revenue and monthly details.
     * @throws CustomException If the owner or restaurant is not found, or if access is forbidden.
     */
    @Override
    public MonthlyRevenueResponse getMonthlyRevenueInYear(int year, long ownerId, long restaurantId) throws CustomException {

        // Validate the owner and restaurant
        validateOwnerAndRestaurant(ownerId, restaurantId);

        // Get the monthly revenue data
        var revenues = orderRepository.getMonthlyRevenueInYear(
                restaurantId, year, OrderStatusConstants.SUCCESSFUL_STATUSES);

        return convertToMonthlyRevenueResponse(revenues);
    }

    /**
     * Retrieves the yearly revenue for a specific restaurant.
     *
     * @param ownerId      The ID of the restaurant owner.
     * @param restaurantId The ID of the restaurant.
     * @return A YearlyRevenueResponse containing the total revenue and yearly details.
     * @throws CustomException If the owner or restaurant is not found, or if access is forbidden.
     */
    @Override
    public YearlyRevenueResponse getYearlyRevenue(long ownerId, long restaurantId) throws CustomException {
        // Validate the owner and restaurant
        validateOwnerAndRestaurant(ownerId, restaurantId);

        // Get the yearly revenue data
        var revenues = orderRepository.getYearlyRevenue(
                restaurantId, OrderStatusConstants.SUCCESSFUL_STATUSES);

        // convert the list of YearlyRevenueDTO to a list of YearlyRevenueDetail
        List<YearlyRevenueDetail> yearlyRevenueDetails = revenues.stream()
                .map(dto -> YearlyRevenueDetail.builder()
                            .year(dto.getYear())
                            .orderCount(dto.getOrderCount())
                            .revenue(dto.getRevenue())
                            .build()
                )
                .toList();

        // Calculate the total revenue
        BigDecimal totalRevenue = yearlyRevenueDetails.stream()
                .map(YearlyRevenueDetail::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return YearlyRevenueResponse.builder()
                .totalRevenue(totalRevenue)
                .yearlyDetails(yearlyRevenueDetails)
                .build();
    }

    /**
     * Validates if the owner has access to the specified restaurant.
     *
     * @param ownerId      The ID of the restaurant owner.
     * @param restaurantId The ID of the restaurant.
     * @throws CustomException If the owner or restaurant is not found, or if access is forbidden.
     */
    private void validateOwnerAndRestaurant(long ownerId, long restaurantId) throws CustomException {
        var owner = userRepository.findById(ownerId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "Owner", String.valueOf(ownerId))
        );
        if (owner.getOwnerRestaurants().stream().noneMatch(r -> r.getId() == restaurantId)
                || !owner.getRole().equals(Role.OWNER)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }
    }

    /**
     * Retrieves the monthly revenue for a specific restaurant within a date range.
     *
     * @param startDate    The start date of the range.
     * @param endDate      The end date of the range.
     * @param ownerId      The ID of the restaurant owner.
     * @param restaurantId The ID of the restaurant.
     * @return A MonthlyRevenueResponse containing the total revenue and monthly details.
     * @throws CustomException If the owner or restaurant is not found, or if access is forbidden.
     */
    @Override
    public MonthlyRevenueResponse getMonthlyRevenueInRange(YearMonth startDate, YearMonth endDate, long ownerId, long restaurantId) throws CustomException {
        // Validate the owner and restaurant
        validateOwnerAndRestaurant(ownerId, restaurantId);

        // Validate the date range
        if (startDate.isAfter(endDate)) {
            throw new CustomException(StatusCode.INVALID_DATE_RANGE);
        }

        // convert YearMonth to OffsetDateTime with timezone Asia/Ho_Chi_Minh
        OffsetDateTime start = startDate.atDay(1).atStartOfDay().atOffset(ZoneOffset.of("+07:00"));
        OffsetDateTime end = endDate.atEndOfMonth().atTime(LocalTime.MAX).atOffset(ZoneOffset.of("+07:00"));

        // Get the monthly revenue data
                var revenues = orderRepository.getMonthlyRevenueInRange(
                        restaurantId, start, end, OrderStatusConstants.SUCCESSFUL_STATUSES);

        return convertToMonthlyRevenueResponse(revenues);
    }

    /**
     * Converts a list of MonthlyRevenueDTO to a MonthlyRevenueResponse.
     *
     * @param revenues The list of MonthlyRevenueDTO.
     * @return A MonthlyRevenueResponse containing the total revenue and monthly details.
     */
    private MonthlyRevenueResponse convertToMonthlyRevenueResponse(List<MonthlyRevenueDTO> revenues) {
        // Convert the list of MonthlyRevenueDTO to a list of MonthlyRevenueDetail
        List<MonthlyRevenueDetail> monthlyRevenueDetails = revenues.stream()
                .map(dto -> MonthlyRevenueDetail.builder()
                        .month(dto.getMonth())
                        .year(dto.getYear())
                        .orderCount(dto.getTotalOrders())
                        .revenue(dto.getTotalRevenue())
                        .build()
                )
                .toList();

        BigDecimal totalRevenue = monthlyRevenueDetails.stream()
                .map(MonthlyRevenueDetail::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return MonthlyRevenueResponse.builder()
                .totalRevenue(totalRevenue)
                .monthlyDetails(monthlyRevenueDetails)
                .build();
    }
}
