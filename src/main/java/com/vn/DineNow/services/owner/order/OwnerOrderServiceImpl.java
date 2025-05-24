package com.vn.DineNow.services.owner.order;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.OrderMapper;
import com.vn.DineNow.payload.request.Order.RejectOrderRequest;
import com.vn.DineNow.payload.request.payment.PaymentRequest;
import com.vn.DineNow.payload.response.order.OrderDetailResponse;
import com.vn.DineNow.repositories.OrderRepository;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.services.common.email.EmailService;
import com.vn.DineNow.services.customer.payment.PaymentService;
import com.vn.DineNow.services.customer.vnpay.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnerOrderServiceImpl implements OwnerOrderService{
    OrderRepository orderRepository;
    UserRepository userRepository;
    RestaurantRepository restaurantRepository;
    OrderMapper orderMapper;
    EmailService emailService;

    /**
     * Validates the status of an order to ensure it is not cancelled or completed.
     * @param order the order to validate
     * @throws CustomException if the order is cancelled or completed
     */
    private void validateOrderStatus(Order order) throws CustomException {
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new CustomException(StatusCode.INVALID_ACTION, "Order is cancelled.");
        }
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new CustomException(StatusCode.INVALID_ACTION, "Order is completed.");
        }
    }

    /**
     * Updates the status of an order.
     * @param ownerId the ID of the owner
     * @param orderId the ID of the order to update
     * @param status the new status to set
     * @return true if the status was updated, false otherwise
     * @throws CustomException if the order is not found or if the status transition is not allowed
     */
    @Override
    public boolean updateOrderStatus(long ownerId, long orderId, OrderStatus status, RejectOrderRequest reason) throws CustomException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Order", String.valueOf(orderId)));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Owner", String.valueOf(ownerId)));

        if (owner.getRole() != Role.OWNER || order.getReservation().getRestaurant().getOwner() != owner) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        validateOrderStatus(order);

        if (!isTransitionAllowed(order.getStatus(), status)) {
            throw new CustomException(StatusCode.INVALID_ACTION, "Order status cannot be updated from " + order.getStatus() + " to " + status);
        }
        if (status.equals(OrderStatus.CONFIRMED)){
            try {
                sendMailConfirmOrder(order);
            } catch (CustomException e) {
                throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
            }
        }
        if (status.equals(OrderStatus.CANCELLED)){
            try {
                sendMailRejectOrder(order, reason.getReason());
            } catch (CustomException e) {
                throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
            }
        }
        order.setStatus(status);
        orderRepository.save(order);
        return true;
    }

    private void sendMailConfirmOrder(Order order) throws CustomException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'lúc' HH:mm");

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("fullName", order.getReservation().getCustomer().getFullName());
        variables.put("restaurantName", order.getReservation().getRestaurant().getName());
        variables.put("orderCode", String.valueOf(order.getId()));
        variables.put("orderTime", order.getCreatedAt().format(formatter));
        variables.put("totalPrice", order.getTotalPrice().toPlainString());
        variables.put("restaurantAddress", order.getReservation().getRestaurant().getAddress());
        variables.put("restaurantPhone", order.getReservation().getRestaurant().getPhone());
        variables.put("reservationTime", order.getReservation().getReservationTime().format(formatter));
        variables.put("paymentLink", "https://www.google.com.vn/?hl=vi");

        emailService.confirmOrderEmail(
                order.getReservation().getCustomer().getEmail(),
                "Đơn hàng đã được xác nhận",
                "confirm-order",
                variables
        );
    }

    private  void sendMailRejectOrder(Order order, String reason) throws CustomException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'lúc' HH:mm");

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("fullName", order.getReservation().getCustomer().getFullName());
        variables.put("restaurantName", order.getReservation().getRestaurant().getName());
        variables.put("orderCode", String.valueOf(order.getId()));
        variables.put("orderTime", order.getCreatedAt().format(formatter));
        variables.put("totalPrice", order.getTotalPrice().toPlainString());
        variables.put("restaurantAddress", order.getReservation().getRestaurant().getAddress());
        variables.put("restaurantPhone", order.getReservation().getRestaurant().getPhone());
        variables.put("reservationTime", order.getReservation().getReservationTime().format(formatter));
        variables.put("rejectionReason", reason);
        variables.put("reorderLink", "https://www.google.com.vn/?hl=vi");
        emailService.rejectOrderEmail(
                order.getReservation().getCustomer().getEmail(),
                "Đơn hàng đã bị từ chối",
                "reject-order",
                variables
        );
    }



    private boolean isTransitionAllowed(OrderStatus current, OrderStatus target) {
        return switch (current) {
            case PENDING -> target == OrderStatus.CONFIRMED || target == OrderStatus.CANCELLED;
            case CONFIRMED -> target == OrderStatus.PAID;
            case PAID -> target == OrderStatus.COMPLETED;
            default -> false;
        };
    }


    private  boolean isTransitionAllowedForCallback(OrderStatus current, OrderStatus target) {
        return switch (current) {
            case CONFIRMED -> target == OrderStatus.PAID || target == OrderStatus.FAILED;
            default -> false;
        };
    }
    /**
     * Retrieves all orders for a specific restaurant and owner with status PAID or CONFIRMED.
     * @param ownerId the ID of the owner
     * @param restaurantId the ID of the restaurant
     * @return a list of OrderResponse objects
     * @throws CustomException if the owner or restaurant is not found or does not have the OWNER role or if the restaurant does not belong to the owner
     */
    @Override
    public List<?> getAllOrderByStatuses(long ownerId, long restaurantId, Set<OrderStatus> statuses) throws CustomException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Owner", String.valueOf(ownerId)));
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Restaurant", String.valueOf(restaurantId)));
        if (owner.getRole() != Role.OWNER || restaurant.getOwner() != owner) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }
        List<Order> orders = orderRepository.findAllByReservation_RestaurantAndStatusInAndReservation_Restaurant_Owner(restaurant, statuses, owner);
        boolean useSimple = isSimpleStatus(statuses);
        return orders.stream()
                .map(useSimple ? orderMapper::toSimpleDTO : orderMapper::toDTO)
                .toList();
    }

    private boolean isSimpleStatus(Set<OrderStatus> statuses) {
        return statuses.size() == 1 &&
                (statuses.contains(OrderStatus.PENDING) || statuses.contains(OrderStatus.CANCELLED));
    }

    /**
     * Retrieves the details of a specific order for a specific owner.
     * @param orderId the ID of the order
     * @param ownerId the ID of the owner
     * @return an OrderDetailResponse object containing order details
     * @throws CustomException if the order or owner is not found or if the owner does not have permission to view the order
     */
    @Override
    public OrderDetailResponse getOrderDetail(long orderId, long ownerId) throws CustomException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Order", String.valueOf(orderId)));
        User owner = userRepository.findById(ownerId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId)));
        if (owner.getRole() != Role.OWNER || order.getReservation().getRestaurant().getOwner() != owner) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new CustomException(StatusCode.INVALID_ACTION, "Order is cancelled.");
        }
        return orderMapper.toDetailDTO(order);
    }

    /**
     * Updates the order status from a callback payment.
     *
     * @param orderId the ID of the order
     * @param status  the new status to set
     * @throws CustomException if the order is not found or if the status transition is not allowed
     */
    @Override
    public void updateOrderStatusFromCallBackPayment(long orderId, OrderStatus status) throws CustomException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Order", String.valueOf(orderId)));

        if (!isTransitionAllowedForCallback(order.getStatus(), status)) {
            throw new CustomException(StatusCode.INVALID_ACTION, "Order status cannot be updated from " + order.getStatus() + " to " + status);
        }
        order.setStatus(status);
        orderRepository.save(order);
    }
}
