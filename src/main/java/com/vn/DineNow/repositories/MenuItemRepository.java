package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.FoodCategory;
import com.vn.DineNow.entities.MainCategory;
import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.entities.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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


}
