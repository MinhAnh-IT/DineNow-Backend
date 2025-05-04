package com.vn.DineNow.repositories;


import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.Review;
import com.vn.DineNow.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByRestaurant(Restaurant restaurant, Pageable pageable);
    List<Review> findAllByRestaurant(Restaurant restaurant);
}
