package com.vn.DineNow.repositories;


import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByNameAndOwner(String name, User owner);
    Page<Restaurant> findAllByStatus(RestaurantStatus status, Pageable pageable);
    List<Restaurant> findAllByType_Id(long typeId, Pageable pageable);
    @Query("SELECT r FROM Restaurant r " +
            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND LOWER(r.address) LIKE LOWER(CONCAT('%', :province, '%'))")
    Page<Restaurant> searchRestaurantByCityAndName(
            @Param("province") String province,
            @Param("name") String name, Pageable pageable
            );

    List<Restaurant> findAllByOwner(User owner);

    @Query(
            value = """
                SELECT r.*
                FROM restaurants r
                LEFT JOIN reservations res ON r.id = res.restaurant_id
                LEFT JOIN orders o ON o.reservation_id = res.id
                WHERE r.status = 'APPROVED'
                GROUP BY r.id, r.name, r.address, r.phone, r.average_rating
                ORDER BY
                    ROUND(
                        r.average_rating * 0.5 +
                        LOG10(COUNT(DISTINCT IF(o.status = 'COMPLETED', o.id, NULL)) + 1) * 0.5,
                        3
                    ) DESC
                LIMIT 15
            """,
            nativeQuery = true
    )
    List<Restaurant> findTopFeaturedRestaurants();


    @Query(value = "SELECT *, " +
            "(6371 * acos(" +
            "cos(radians(:lat)) * cos(radians(latitude)) * " +
            "cos(radians(:lng) - radians(longitude)) + " +
            "sin(radians(:lat)) * sin(radians(latitude))" +
            ")) AS distance " +
            "FROM restaurants " +
            "WHERE status = 'APPROVED' " +
            "HAVING distance < :radius " +
            "ORDER BY distance ASC", nativeQuery = true)
    List<Restaurant> findRestaurantsWithinRadius(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radius);

}
