package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.payload.projection.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
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

    @Query("""
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
    """)
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

    @Query("""
        SELECT
            :month AS month,
            :year AS year,
            res.name AS restaurantName,
            COALESCE(SUM(o.totalPrice), 0) AS totalRevenue,
            COALESCE(COUNT(DISTINCT o.id), 0) AS totalOrders
        FROM Restaurant res
        LEFT JOIN res.restaurantReservations r
            ON MONTH(r.reservationTime) = :month AND YEAR(r.reservationTime) = :year
        LEFT JOIN Order o ON o.reservation = r AND o.status IN :statuses
        GROUP BY res.id, res.name
        ORDER BY res.id
    """)
    List<MonthlyRevenueDTO> getAllRestaurantMonthlyRevenue(
            @Param("month") int month,
            @Param("year") int year,
            @Param("statuses") Set<OrderStatus> statuses
    );

    @Query("""
            SELECT
                res.name AS restaurantName,
                COALESCE(SUM(o.totalPrice), 0) AS totalRevenue,
                COALESCE(COUNT(DISTINCT o.id), 0) AS totalOrders
            FROM Restaurant res
            LEFT JOIN res.restaurantReservations r ON r.reservationTime BETWEEN :start AND :end
            LEFT JOIN Order o ON o.reservation = r AND o.status IN :statuses
            GROUP BY res.id, res.name
            ORDER BY res.id
        """)
    List<TotalRevenueDTO> getMonthlyRevenueOfAllRestaurantsInRange(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("statuses") Set<OrderStatus> statuses
    );

    @Query("""
        SELECT
            res.name AS restaurantName,
            COALESCE(SUM(o.totalPrice), 0) AS totalRevenue,
            COALESCE(COUNT(DISTINCT o.id), 0) AS totalOrders
        FROM Restaurant res
        LEFT JOIN res.restaurantReservations r
        LEFT JOIN Order o ON o.reservation = r AND o.status IN :statuses
        GROUP BY res.id, res.name
    """)
    List<TotalRevenueDTO> getTotalRevenuePerRestaurantSinceJoined(
            @Param("statuses") Set<OrderStatus> statuses
    );

    @Query("""
            SELECT
                :month AS month,
                :year AS year,
                res.name AS restaurantName,
                COALESCE(SUM(CASE WHEN o.id IS NOT NULL THEN r.numberOfPeople ELSE 0 END), 0) AS totalGuests,
                tier.feePerGuest AS feePerGuest,
                COALESCE(SUM(CASE WHEN o.id IS NOT NULL THEN r.numberOfPeople * tier.feePerGuest ELSE 0 END), 0) AS totalProfit,
                COUNT(DISTINCT o.id) AS totalOrders
            FROM Restaurant res
            JOIN res.restaurantTier tier
            LEFT JOIN res.restaurantReservations r ON MONTH(r.reservationTime) = :month AND YEAR(r.reservationTime) = :year
            LEFT JOIN Order o ON o.reservation = r AND o.status IN :statuses
            GROUP BY res.id, res.name, tier.feePerGuest
            ORDER BY res.id
        """)
    List<MonthlyProfitDTO> getMonthlyProfitsOfAllRestaurants(
            @Param("month") int month,
            @Param("year") int year,
            @Param("statuses") Set<OrderStatus> successfulStatuses
    );



    @Query("""
            SELECT
                res.name AS restaurantName,
                COALESCE(SUM(CASE WHEN o.id IS NOT NULL THEN r.numberOfPeople ELSE 0 END), 0) AS totalGuests,
                tier.feePerGuest AS feePerGuest,
                COALESCE(SUM(CASE WHEN o.id IS NOT NULL THEN r.numberOfPeople * tier.feePerGuest ELSE 0 END), 0) AS totalProfit,
                COUNT(DISTINCT o.id) AS totalOrders
            FROM Restaurant res
            JOIN res.restaurantTier tier
            LEFT JOIN res.restaurantReservations r
            LEFT JOIN Order o ON o.reservation = r AND o.status IN :statuses
            GROUP BY res.id, res.name, tier.feePerGuest
            ORDER BY res.id
        """)
    List<TotalProfitDTO> getTotalProfitsPerRestaurant(@Param("statuses") Set<OrderStatus> successfulStatuses);

    @Query("""
            SELECT
                res.name AS restaurantName,
                COALESCE(SUM(CASE WHEN o.id IS NOT NULL THEN r.numberOfPeople ELSE 0 END), 0) AS totalGuests,
                tier.feePerGuest AS feePerGuest,
                COALESCE(SUM(CASE WHEN o.id IS NOT NULL THEN r.numberOfPeople * tier.feePerGuest ELSE 0 END), 0) AS totalProfit,
                COUNT(DISTINCT o.id) AS totalOrders
            FROM Restaurant res
            JOIN res.restaurantTier tier
            LEFT JOIN res.restaurantReservations r
                ON r.reservationTime BETWEEN :start AND :end
            LEFT JOIN Order o ON o.reservation = r AND o.status IN :statuses
            GROUP BY res.id, res.name, tier.feePerGuest
            ORDER BY res.id
        """)
    List<TotalProfitDTO> getTotalProfitsPerRestaurantInRange(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("statuses") Set<OrderStatus> successfulStatuses
    );

    @Query("SELECT o FROM Order o WHERE o.status = 'CONFIRMED' AND o.updatedAt <= :now")
    List<Order> findExpiredUnpaidOrders(@Param("now") OffsetDateTime now);

    @Query("""
    SELECT o FROM Order o
    JOIN o.reservation r
    JOIN r.restaurant rest
    WHERE rest = :restaurant
      AND o.status IN :statuses
      AND r.reservationTime BETWEEN :startDateTime AND :endDateTime
      AND NOT EXISTS (
          SELECT 1 FROM RestaurantPayment rp
          WHERE rp.restaurant = :restaurant
            AND rp.startDate = :startDate
            AND rp.endDate = :endDate
      )
""")
    List<Order> findUnsettledOrdersByRestaurantAndPeriod(
            @Param("restaurant") Restaurant restaurant,
            @Param("statuses") Set<OrderStatus> statuses,
            @Param("startDateTime") OffsetDateTime startDateTime,
            @Param("endDateTime") OffsetDateTime endDateTime,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
