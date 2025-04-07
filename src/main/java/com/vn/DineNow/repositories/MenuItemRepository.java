package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    MenuItem findFirstByRestaurant(Restaurant restaurant);

}
