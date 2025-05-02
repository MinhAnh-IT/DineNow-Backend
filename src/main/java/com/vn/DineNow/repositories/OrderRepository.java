package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByReservation_RestaurantAndReservation_customer(Restaurant restaurant, User customer);
    List<Order> findAllByReservation_Customer(User owner);
    List<Order> findAllByReservation_CustomerAndStatusIn(User owner, Set<OrderStatus> status);
    List<Order> findAllByReservation_RestaurantAndStatusInAndReservation_Restaurant_Owner(Restaurant restaurant, Set<OrderStatus> status, User owner);
    List<Order> findAllByStatus(OrderStatus status, Pageable pageable);

}
