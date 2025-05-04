package com.vn.DineNow.services.owner.order;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.OrderMapper;
import com.vn.DineNow.payload.response.order.OrderDetailResponse;
import com.vn.DineNow.payload.response.order.OrderResponse;
import com.vn.DineNow.payload.response.order.OrderSimpleResponse;
import com.vn.DineNow.repositories.OrderRepository;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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

    /**
     * Cancels an order by setting its status to CANCELLED.
     * @param orderId the ID of the order to cancel
     * @return true if the order was cancelled, false otherwise
     * @throws Exception if the order is not found or if the status is not PENDING
     */
    @Override
    public boolean cancelOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Order", String.valueOf(orderId)));
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    /**
     * Confirms an order by setting its status to CONFIRMED.
     * @param orderId the ID of the order to confirm
     * @return true if the order was confirmed, false otherwise
     * @throws Exception if the order is not found or if the status is not PENDING
     */
    @Override
    public boolean confirmedOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Order", String.valueOf(orderId)));
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    /**
     * Completes an order by setting its status to COMPLETED.
     * @param orderId the ID of the order to complete
     * @return true if the order was completed, false otherwise
     * @throws Exception if the order is not found or if the status is not PAID
     */
    @Override
    public boolean completeOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Order", String.valueOf(orderId)));
        if (order.getStatus() == OrderStatus.PAID) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

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
    public boolean updateOrderStatus(long ownerId, long orderId, OrderStatus status) throws CustomException {
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

        order.setStatus(status);
        orderRepository.save(order);
        return true;
    }


    private boolean isTransitionAllowed(OrderStatus current, OrderStatus target) {
        return switch (current) {
            case PENDING -> target == OrderStatus.CONFIRMED || target == OrderStatus.CANCELLED;
            case CONFIRMED -> target == OrderStatus.PAID;
            case PAID -> target == OrderStatus.COMPLETED;
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
}
