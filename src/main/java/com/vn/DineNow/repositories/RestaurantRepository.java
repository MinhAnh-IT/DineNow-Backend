package com.vn.DineNow.repositories;


import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Restaurant findFirstByOwner(User user);

}
