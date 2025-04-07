package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.DiningTable;
import com.vn.DineNow.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {

    DiningTable findFirstByRestaurant(Restaurant restaurant);

}
