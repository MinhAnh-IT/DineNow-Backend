package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    OrderItem findFirstByOrder(Order order);

    OrderItem findFirstByMenuItem(MenuItem menuItem);

}
