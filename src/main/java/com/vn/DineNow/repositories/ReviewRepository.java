package com.vn.DineNow.repositories;


import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.Review;
import com.vn.DineNow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findFirstByCustomer(User user);

    Review findFirstByRestaurant(Restaurant restaurant);

}
