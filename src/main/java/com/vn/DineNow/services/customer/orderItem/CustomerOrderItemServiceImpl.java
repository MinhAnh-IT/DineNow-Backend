package com.vn.DineNow.services.customer.orderItem;

import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.OrderItem;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.OrderItem.OrderItemRequest;
import com.vn.DineNow.payload.response.OrderItem.OrderItemResponse;
import com.vn.DineNow.repositories.MenuItemRepository;
import com.vn.DineNow.repositories.OrderItemRepository;
import com.vn.DineNow.repositories.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerOrderItemServiceImpl implements CustomerOrderItemService{
    OrderItemRepository orderItemRepository;
    OrderRepository orderRepository;
    MenuItemRepository menuItemRepository;

    /**
     * Creates a new order item for the given order.
     *
     * @param order the order to which the item belongs
     * @param orderItemRequest the request object containing menu item ID and quantity
     * @throws Exception if the menu item is not found or order is null
     */
    @Override
    public OrderItem createOrderItem(Order order, OrderItemRequest orderItemRequest) throws Exception {
        // Validate order and menuItem
        MenuItem menuItem = menuItemRepository.findById(orderItemRequest.getMenuItemId())
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "MenuItem", String.valueOf(orderItemRequest.getMenuItemId())));
        if (order == null ) {
            throw new CustomException(StatusCode.INVALID_ENTITY, "Order cannot be null");
        }

        // Create OrderItem entity
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(orderItemRequest.getQuantity());
        orderItem.setPrice(menuItem.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity())));

        // Save OrderItem to the database
        orderItemRepository.save(orderItem);
        return orderItem;
    }

    @Override
    public long getTotalPrice(List<OrderItemRequest> requests) throws Exception {
        long totalPrice = 0;
        for (OrderItemRequest request : requests) {
            // Find the menu item by ID
            MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                    .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "MenuItem", String.valueOf(request.getMenuItemId())));

            // Calculate the total price for this item
            long itemTotalPrice = menuItem.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())).longValue();
            totalPrice += itemTotalPrice;
        }
        return totalPrice;
    }
}
