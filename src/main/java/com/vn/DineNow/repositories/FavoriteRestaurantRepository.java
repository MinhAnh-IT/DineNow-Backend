package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.FavoriteRestaurant;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {

    FavoriteRestaurant findFirstByUser(User user);

    FavoriteRestaurant findFirstByRestaurant(Restaurant restaurant);

}
