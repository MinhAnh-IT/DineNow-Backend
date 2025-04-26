package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {
    boolean existsByNameAndRestaurant_Id(String name, long restaurantId);
    List<FoodCategory> findAllByRestaurant_Id(long restaurantId);
}
