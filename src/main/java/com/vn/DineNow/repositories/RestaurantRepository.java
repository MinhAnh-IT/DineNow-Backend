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
    @Query("SELECT r FROM Restaurant r " +
            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND LOWER(r.address) LIKE LOWER(CONCAT('%', :province, '%'))")
    Page<Restaurant> searchRestaurantByCityAndName(
            @Param("province") String province,
            @Param("name") String name, Pageable pageable
            );

    List<Restaurant> findAllByOwner(User owner);
}
