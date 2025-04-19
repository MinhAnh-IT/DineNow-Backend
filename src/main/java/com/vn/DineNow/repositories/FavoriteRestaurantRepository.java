package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.FavoriteRestaurant;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {

    List<FavoriteRestaurant> findByUserAndRestaurantStatusAndUserRole(User user, RestaurantStatus status, Role role);
    boolean existsByUserAndRestaurant(User user, Restaurant restaurant);
    @Modifying
    @Query("DELETE FROM FavoriteRestaurant fr WHERE fr.user = :user AND fr.restaurant = :restaurant")
    int deleteByUserAndRestaurant(@Param("user") User user, @Param("restaurant") Restaurant restaurant);
    Optional<FavoriteRestaurant> findByUserAndRestaurant(User user, Restaurant restaurant);
}
