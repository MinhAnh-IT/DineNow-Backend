package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.FoodCategory;
import com.vn.DineNow.entities.MainCategory;
import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.entities.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
