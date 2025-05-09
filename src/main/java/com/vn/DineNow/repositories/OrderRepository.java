package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.payload.projection.MonthlyRevenueDTO;
import com.vn.DineNow.payload.projection.YearlyRevenueDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByReservation_RestaurantAndReservation_customer(Restaurant restaurant, User customer);
    List<Order> findAllByReservation_Customer(User owner);
    List<Order> findAllByReservation_CustomerAndStatusIn(User owner, Set<OrderStatus> status);
    List<Order> findAllByReservation_RestaurantAndStatusInAndReservation_Restaurant_Owner(Restaurant restaurant, Set<OrderStatus> status, User owner);
    List<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    @Query("""
                SELECT
                    MONTH(r.reservationTime) AS month,
                    YEAR(r.reservationTime) AS year,
                    SUM(o.totalPrice) AS totalRevenue,
                    COUNT(DISTINCT o.id) AS totalOrders
                FROM Order o
                JOIN o.reservation r
                JOIN r.restaurant res
                WHERE res.id = :restaurantId
                  AND YEAR(r.reservationTime) = :year
                  AND o.status IN :statuses
                GROUP BY MONTH(r.reservationTime), YEAR(r.reservationTime)
                ORDER BY MONTH(r.reservationTime)
            """)
    List<MonthlyRevenueDTO> getMonthlyRevenueInYear(
            @Param("restaurantId") long restaurantId,
            @Param("year") int year,
            @Param("statuses") Set<OrderStatus> statuses
    );


    @Query(
            """
                SELECT
                    YEAR(r.reservationTime) AS year,
                    SUM(o.totalPrice) AS revenue,
                    COUNT(DISTINCT o.id) AS orderCount
                FROM Order o
                JOIN o.reservation r
                JOIN r.restaurant res
                WHERE res.id = :restaurantId
                  AND o.status IN :statuses
                GROUP BY YEAR(r.reservationTime)
                ORDER BY YEAR(r.reservationTime)
            """
    )
    List<YearlyRevenueDTO> getYearlyRevenue(
            @Param("restaurantId") long restaurantId,
            @Param("statuses") Set<OrderStatus> statuses
    );


    @Query("""
                SELECT
                    MONTH(r.reservationTime) AS month,
                    YEAR(r.reservationTime) AS year,
                    SUM(o.totalPrice) AS totalRevenue,
                    COUNT(DISTINCT o.id) AS totalOrders
                FROM Order o
                JOIN o.reservation r
                JOIN r.restaurant res
                WHERE res.id = :restaurantId
                  AND r.reservationTime BETWEEN :start AND :end
                  AND o.status IN :statuses
                GROUP BY YEAR(r.reservationTime), MONTH(r.reservationTime)
                ORDER BY YEAR(r.reservationTime), MONTH(r.reservationTime)
            """)
    List<MonthlyRevenueDTO> getMonthlyRevenueInRange(
            @Param("restaurantId") long restaurantId,
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("statuses") Set<OrderStatus> statuses
    );




}
