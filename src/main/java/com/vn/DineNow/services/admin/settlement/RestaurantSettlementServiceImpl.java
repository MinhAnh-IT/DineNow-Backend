package com.vn.DineNow.services.admin.settlement;

import com.vn.DineNow.constrants.OrderStatusConstants;
import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.RestaurantPaymentMapper;
import com.vn.DineNow.payload.request.restaurantPayment.RestaurantPaymentRequest;
import com.vn.DineNow.payload.response.restaurantPayment.RestaurantPaymentResponse;
import com.vn.DineNow.payload.response.settlement.RestaurantSettlementInfoDTO;
import com.vn.DineNow.payload.response.settlement.SettlementSummaryDTO;
import com.vn.DineNow.repositories.OrderRepository;
import com.vn.DineNow.repositories.RestaurantPaymentRepository;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.services.common.fileService.FileService;
import com.vn.DineNow.services.owner.bankAccount.BankAccountService;
import org.springframework.transaction.annotation.Transactional;
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
public class RestaurantSettlementServiceImpl implements RestaurantSettlementService {

    OrderRepository orderRepository;
    RestaurantPaymentRepository restaurantPaymentRepository;
    RestaurantRepository restaurantRepository;
    BankAccountService bankAccountService;
    RestaurantPaymentMapper restaurantPaymentMapper;


    /**
     * Retrieves the list of restaurants that have not been settled for the current settlement period
     * (either from the 1st–15th or 16th–end of the month).
     *
     * @return a list of unsettled restaurant info DTOs
     * @throws CustomException if any data retrieval error occurs
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestaurantSettlementInfoDTO> findUnsettledRestaurantsForCurrentPeriod() throws CustomException {
        LocalDate[] period = getCurrentSettlementPeriod();
        LocalDate startDate = period[0];
        LocalDate endDate = period[1];

        var restaurants = restaurantPaymentRepository.findRestaurantsToSettle(startDate, endDate);

        return restaurants.stream()
                .map(res -> RestaurantSettlementInfoDTO.builder()
                        .restaurantId(res.getRestaurantId())
                        .restaurantName(res.getRestaurantName())
                        .address(res.getAddress())
                        .phoneNumber(res.getPhoneNumber())
                        .build())
                .toList();
    }

    /**
     * Retrieves the settlement summary for a specific restaurant in the current period.
     * Includes total revenue, platform fee, settlement amount, and bank account info.
     *
     * @param restaurantId the ID of the restaurant
     * @return settlement summary details for confirmation
     * @throws CustomException if the restaurant is not found or there are no valid orders
     */
    @Override
    @Transactional(readOnly = true)
    public SettlementSummaryDTO getSettlementSummary(Long restaurantId) throws CustomException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", restaurantId.toString()));

        LocalDate[] period = getCurrentSettlementPeriod();
        ZoneId zone = ZoneId.systemDefault();

        OffsetDateTime startDateTime = period[0].atStartOfDay(zone).toOffsetDateTime();
        OffsetDateTime endDateTime = period[1].atTime(LocalTime.MAX).atZone(zone).toOffsetDateTime();

        var orders = orderRepository.findUnsettledOrdersByRestaurantAndPeriod(
                restaurant,
                OrderStatusConstants.SUCCESSFUL_STATUSES,
                startDateTime,
                endDateTime,
                period[0],
                period[1]
        );


        return processOrders(orders);
    }

    /**
     * Creates a new restaurant settlement record for the current settlement period.
     *
     * @param request the settlement request containing restaurantId, amount, and note
     * @return the response DTO of the created restaurant payment
     * @throws CustomException if the restaurant does not exist
     */
    @Override
    public RestaurantPaymentResponse createRestaurantPayment(RestaurantPaymentRequest request) throws CustomException {
        var restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", request.getRestaurantId().toString()));

        var restaurantPayment = restaurantPaymentMapper.toEntity(request);
        LocalDate[] period = getCurrentSettlementPeriod();
        restaurantPayment.setRestaurant(restaurant);
        restaurantPayment.setStartDate(period[0]);
        restaurantPayment.setEndDate(period[1]);

        restaurantPaymentRepository.save(restaurantPayment);
        return restaurantPaymentMapper.toResponse(restaurantPayment);
    }

    /**
     * Retrieves all restaurant settlement records that were completed in a specific period,
     * based on the given month and period index (1 = 1st–15th, 2 = 16th–end of month).
     *
     * @param yearMonth the target year and month
     * @param periodIndex 1 for the first half of the month, 2 for the second half
     * @return a list of restaurant payment response DTOs
     * @throws CustomException if the period index is invalid or query fails
     */
    @Override
    public List<RestaurantPaymentResponse> findSettledRestaurantsInPeriod(YearMonth yearMonth, int periodIndex) throws CustomException {
        if (periodIndex != 1 && periodIndex != 2) {
            throw new CustomException(StatusCode.INVALID_PERIOD_INDEX);
        }

        LocalDate startDate;
        LocalDate endDate;

        if (periodIndex == 1) {
            startDate = yearMonth.atDay(1);
            endDate = yearMonth.atDay(15);
        } else {
            startDate = yearMonth.atDay(16);
            endDate = yearMonth.atEndOfMonth();
        }

        var restaurantPayments = restaurantPaymentRepository.findAllByStartDateAndEndDate(startDate, endDate);
        return restaurantPayments.stream()
                .map(restaurantPaymentMapper::toResponse)
                .toList();
    }


    /**
     * Processes a list of orders to calculate settlement information,
     * including total orders, total revenue, platform fee, and final settlement amount.
     *
     * @param orders the list of eligible orders to process
     * @return a settlement summary DTO
     * @throws CustomException if the list of orders is empty
     */
    private SettlementSummaryDTO processOrders(List<Order> orders) throws CustomException {
        if (orders.isEmpty()) {
            throw new CustomException(StatusCode.NOT_FOUND, "order", "No orders found for settlement");
        }

        var restaurant = orders.get(0).getReservation().getRestaurant();
        var bankAccount = bankAccountService.getBankAccount(restaurant.getOwner().getId());

        long totalOrders = orders.size();

        BigDecimal totalRevenue = orders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal platformFee = orders.stream()
                .map(this::calculatePlatformFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal amountToSettle = totalRevenue.subtract(platformFee);

        return SettlementSummaryDTO.builder()
                .restaurantId(restaurant.getId())
                .restaurantName(restaurant.getName())
                .bankName(bankAccount.getBankName())
                .accountHolderName(bankAccount.getAccountHolderName())
                .accountNumber(bankAccount.getAccountNumber())
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .platformFee(platformFee)
                .amountToSettle(amountToSettle)
                .build();
    }

    /**
     * Calculates the platform fee for a given order,
     * based on the number of people and the restaurant's tier configuration.
     *
     * @param order the order for which to calculate the platform fee
     * @return the calculated fee amount
     */
    private BigDecimal calculatePlatformFee(Order order) {
        BigDecimal feePerGuest = order.getReservation().getRestaurant().getRestaurantTier().getFeePerGuest();
        int numberOfPeople = order.getReservation().getNumberOfPeople();
        return feePerGuest.multiply(BigDecimal.valueOf(numberOfPeople));
    }


    /**
     * Determines the current settlement period based on today's date.
     * If today is on or before the 15th, returns [1st, 15th]; otherwise, [16th, end of month].
     *
     * @return an array of two LocalDate objects: [startDate, endDate]
     */
    private LocalDate[] getCurrentSettlementPeriod() {
        LocalDate today = LocalDate.now();
        if (today.getDayOfMonth() <= 15) {
            return new LocalDate[]{ today.withDayOfMonth(1), today.withDayOfMonth(15) };
        } else {
            return new LocalDate[]{ today.withDayOfMonth(16), today.withDayOfMonth(today.lengthOfMonth()) };
        }
    }
}
