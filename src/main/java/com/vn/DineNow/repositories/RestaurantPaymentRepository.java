package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.RestaurantPayment;
import com.vn.DineNow.payload.projection.RestaurantSettlementInfoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantPaymentRepository extends JpaRepository<RestaurantPayment, Long> {
    @Query(value = """
            SELECT DISTINCT r.id AS restaurantId,
                   r.name AS restaurantName,
                   r.address AS address,
                   r.phone AS phoneNumber
            FROM restaurants r
            JOIN reservations res ON res.restaurant_id = r.id
            JOIN orders o ON o.reservation_id = res.id
            WHERE o.status = 'PAID' OR o.status = 'COMPLETED'
              AND res.reservation_time BETWEEN :startDate AND :endDate
              AND r.id NOT IN (
                  SELECT rp.restaurant_id
                  FROM restaurant_payments rp
                  WHERE rp.start_date = :startDate
                    AND rp.end_date = :endDate
              )
        """, nativeQuery = true)
    List<RestaurantSettlementInfoView> findRestaurantsToSettle(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<RestaurantPayment> findAllByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
}
