package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.entities.OrderItem;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findTopByOrder_Reservation_CustomerAndMenuItemAndOrder_StatusAndReviewIsNull

            (
            User customer,
            MenuItem menuItem,
            OrderStatus status
    );

}
