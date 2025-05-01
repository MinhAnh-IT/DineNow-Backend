package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.OrderStatus;
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
    List<Order> findAllByReservation_Restaurant_OwnerAndStatus(User owner, OrderStatus status);
    List<Order> findAllByReservation_CustomerAndStatusIn(User owner, Set<OrderStatus> status);
    @Query("SELECT O FROM Order O WHERE O.reservation.restaurant = :res AND O.status = :status AND O.reservation.restaurant.owner = :owner")
    List<Order> findAllByRestaurantAndStatusForOwner(@Param("res") Restaurant restaurant, @Param("status") OrderStatus status, @Param("owner") User owner);
    List<Order> findAllByReservation_RestaurantAndStatusInAndReservation_Restaurant_Owner(Restaurant restaurant, Set<OrderStatus> status, User owner);


}
