package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.*;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Optional<MenuItem> findByIdAndAvailableTrue(long id);
    Boolean existsByNameAndRestaurant(String name, Restaurant restaurant);
    List<MenuItem> findAllByRestaurantAndAvailableTrue(Restaurant restaurant);
    Page<MenuItem> findAllByAvailableTrue(Pageable pageable);
    List<MenuItem> findAllByRestaurant(Restaurant restaurant);
    Page<MenuItem> findAllByCategory_MainCategoryAndAvailableTrue(MainCategory mainCategory, Pageable pageable);
    Page<MenuItem> findAllByCategoryAndAvailableTrue(FoodCategory category, Pageable pageable);
    @Query(
            value = """
                    SELECT * FROM (
                        SELECT
                            m.*,
                            r.name AS restaurant_name,
                            COUNT(DISTINCT IF(o.status = 'COMPLETED', o.id, NULL)) AS total_orders,
                            ROUND(
                                m.average_rating * 0.5 +
                                LOG10(COUNT(DISTINCT IF(o.status = 'COMPLETED', o.id, NULL)) + 1) * 0.5,
                                3
                            ) AS score,
                            ROW_NUMBER() OVER (PARTITION BY m.restaurant_id ORDER BY 
                                m.average_rating * 0.5 + 
                                LOG10(COUNT(DISTINCT IF(o.status = 'COMPLETED', o.id, NULL)) + 1) * 0.5 DESC
                            ) AS rn
                        FROM menu_items m
                        JOIN restaurants r ON m.restaurant_id = r.id
                        LEFT JOIN order_items oi ON m.id = oi.menu_item_id
                        LEFT JOIN orders o ON o.id = oi.order_id
                        WHERE m.available = TRUE
                          AND r.status = 'APPROVED'
                        GROUP BY m.id, r.name, m.average_rating, m.restaurant_id
                    ) ranked
                    WHERE ranked.rn = 1
                    ORDER BY score DESC
                    LIMIT 15
                    """,
            nativeQuery = true)
    List<MenuItem> findTopFeaturedMenuItems();


    @Query("""
                SELECT mi
                FROM MenuItem mi
                    JOIN mi.restaurant r
                    JOIN mi.category c
                WHERE (:city IS NULL OR LOWER(r.address) LIKE LOWER(CONCAT('%', :city, '%')))
                  AND (:district IS NULL OR LOWER(r.address) LIKE LOWER(CONCAT('%', :district, '%')))
                  AND (:restaurantType IS NULL OR r.type = :restaurantType)
                  AND (:mainCategory IS NULL OR c.mainCategory = :mainCategory)
                  AND (:minPrice IS NULL OR mi.price >= :minPrice)
                  AND (:maxPrice IS NULL OR mi.price <= :maxPrice)
                  AND mi.available = true
            """)
    Page<MenuItem> findAllMenuItemByFilter(
            @Param("city") String city,
            @Param("district") String district,
            @Param("restaurantType") RestaurantType restaurantType,
            @Param("mainCategory") MainCategory mainCategory,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );

}
