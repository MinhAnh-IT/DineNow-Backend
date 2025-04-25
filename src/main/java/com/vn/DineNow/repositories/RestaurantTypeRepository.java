package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.RestaurantType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTypeRepository extends JpaRepository<RestaurantType, Long> {
    boolean existsByName(String name);
}
