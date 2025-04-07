package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Long> {
    RestaurantImage findFirstByRestaurant(Restaurant restaurant);
}
