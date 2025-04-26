package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.RestaurantTiers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTierRepository extends JpaRepository<RestaurantTiers, Long> {
}
