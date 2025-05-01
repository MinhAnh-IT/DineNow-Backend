package com.vn.DineNow.services.customer.order;

import com.vn.DineNow.entities.*;
import com.vn.DineNow.enums.*;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.OrderMapper;
import com.vn.DineNow.mapper.ReservationMapper;
import com.vn.DineNow.payload.request.Order.OrderRequest;
import com.vn.DineNow.payload.request.OrderItem.OrderItemRequest;
import com.vn.DineNow.payload.response.order.OrderSimpleResponse;
import com.vn.DineNow.repositories.OrderRepository;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.services.customer.orderItem.CustomerOrderItemService;
import com.vn.DineNow.services.customer.reservation.CustomerReservationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerOrderServiceImpl implements CustomerOrderService {

    CustomerOrderItemService customerOrderItemService;
    CustomerReservationService customerReservationService;
    CustomerOrderItemService orderItemService;
    ReservationMapper reservationMapper;
    OrderRepository orderRepository;
    UserRepository userRepository;
    RestaurantRepository restaurantRepository;
    OrderMapper orderMapper;

    /**
     * Creates a new order for a customer.
     */
    @Override
    public OrderSimpleResponse createOrder(long customerId, long restaurantId, OrderRequest orderRequest) throws CustomException {
        validateCustomerAndRequest(customerId, orderRequest);

        Reservation reservation = null;
        Order order = null;
        try {
            reservation = createReservation(customerId, restaurantId, orderRequest);
            order = buildOrder(orderRequest, reservation);
        } catch (Exception e) {
            throw new CustomException(StatusCode.BAD_REQUEST);
        }


        Order finalOrder = order;
        Set<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(item -> {
                    try {
                        return createOrderItemSafe(finalOrder, item);
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
        order.setOrderOrderItems(orderItems);

        attachRestaurantToReservation(reservation, restaurantId);

        log.info("Order #{} successfully created for customer #{}", order.getId(), customerId);
        return orderMapper.toSimpleDTO(order);
    }

    /**
     * Retrieves all orders for a specific restaurant and customer.
     * @param customerId the ID of the customer
     * @param restaurantId the ID of the restaurant
     * @return a list of OrderResponse objects
     */
    @Override
    public List<OrderSimpleResponse> getAllOrderByRestaurantAndCustomer(long customerId, long restaurantId) throws CustomException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Restaurant", String.valueOf(restaurantId)));

        User user = userRepository.findById(customerId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "customer", String.valueOf(customerId))
        );
        List<Order> orders = orderRepository.findAllByReservation_RestaurantAndReservation_customer(restaurant, user);

        return orders.stream()
                .map(orderMapper::toSimpleDTO)
                .toList();
    }


    /**
     * Retrieves all orders for a specific customer.
     * @param ownerId the ID of the customer
     * @return a list of OrderSimpleResponse objects
     */
    @Override
    public List<OrderSimpleResponse> getAllOrderByCustomer(long ownerId) throws CustomException {
        User customer = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Customer", String.valueOf(ownerId)));
        if (customer.getRole() != Role.CUSTOMER) {
            throw new CustomException(StatusCode.INVALID_ACTION, "User is not a customer.");
        }
        List<Order> orders = orderRepository.findAllByReservation_Customer(customer);
        return orders.stream()
                .map(orderMapper::toSimpleDTO)
                .toList();
    }

    /**
     * Retrieves all orders for a specific customer with status PENDING or PAID.
     * @param ownerId the ID of the customer
     * @return a list of OrderSimpleResponse objects
     */
    @Override
    public List<OrderSimpleResponse> getAllOrderByStatusPendingAndPaid(long ownerId, Set<OrderStatus> statuses) throws CustomException {
        User customer = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Customer", String.valueOf(ownerId)));
        if (customer.getRole() != Role.CUSTOMER) {
            throw new CustomException(StatusCode.INVALID_ACTION, "User is not a customer.");
        }
        List<Order> orders = orderRepository.findAllByReservation_CustomerAndStatusIn(customer, statuses);
        return orders.stream()
                .map(orderMapper::toSimpleDTO)
                .toList();
    }

    /**
     * Cancels an order for a customer.
     * @param ownerId the ID of the customer
     * @param orderId the ID of the order to cancel
     * @return true if the order was successfully cancelled
     */
    @Override
    public boolean cancelOrder(long ownerId, Long orderId) throws CustomException {
        User customer = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Customer", String.valueOf(ownerId)));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Order", String.valueOf(orderId)));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new CustomException(StatusCode.INVALID_ACTION, "Order is not in a cancellable state.");
        }
        if (!order.getReservation().getCustomer().getId().equals(customer.getId()) || !customer.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(StatusCode.INVALID_ACTION, "User is not a customer or does not own this order.");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return true;
    }

    /**
     * Validates customer and order request.
     */
    private void validateCustomerAndRequest(long customerId, OrderRequest orderRequest) throws CustomException {
        if (orderRequest.getOrderItems() == null || orderRequest.getOrderItems().isEmpty()) {
            throw new CustomException(StatusCode.INVALID_ACTION, "Order must contain at least one item.");
        }

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Customer", String.valueOf(customerId)));

        if (customer.getRole() != Role.CUSTOMER) {
            throw new CustomException(StatusCode.INVALID_ACTION, "User is not a customer.");
        }
    }

    /**
     * Creates a reservation based on order request.
     */
    private Reservation createReservation(long customerId, long restaurantId, OrderRequest request) throws Exception {
        var reservationRequest = reservationMapper.toRequestDTO(request);
        reservationRequest.setCustomerId(customerId);
        reservationRequest.setRestaurantId(restaurantId);
        return customerReservationService.createReservation(reservationRequest);
    }

    /**
     * Builds and saves a new Order.
     */
    private Order buildOrder(OrderRequest orderRequest, Reservation reservation) throws Exception {
        Order order = orderMapper.toEntity(orderRequest);
        long totalPrice = orderItemService.getTotalPrice(orderRequest.getOrderItems());

        order.setTotalPrice(BigDecimal.valueOf(totalPrice));
        order.setStatus(OrderStatus.PENDING);
        order.setReservation(reservation);
        order.setCreatedAt(OffsetDateTime.now());
        order.setUpdatedAt(OffsetDateTime.now());

        return orderRepository.save(order);
    }

    /**
     * Binds restaurant entity to the reservation (for mapping response).
     */
    private void attachRestaurantToReservation(Reservation reservation, long restaurantId) throws CustomException {
        reservation.setRestaurant(
                restaurantRepository.findById(restaurantId)
                        .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Restaurant", String.valueOf(restaurantId)))
        );
    }

    /**
     * Wraps order item creation in safe error handling block.
     */
    private OrderItem createOrderItemSafe(Order order, OrderItemRequest itemRequest) throws CustomException {
        try {
            return customerOrderItemService.createOrderItem(order, itemRequest);
        } catch (Exception e) {
            log.error("Failed to create order item: {}", e.getMessage(), e);
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR, "Error while creating order item.");
        }
    }
}
