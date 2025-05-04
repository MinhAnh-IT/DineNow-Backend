package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.entities.MenuItemReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemReviewRepository extends JpaRepository<MenuItemReview, Long> {
    Page<MenuItemReview> findAllByMenuItem(MenuItem menuItem, Pageable pageable);
    List<MenuItemReview> findAllByMenuItem(MenuItem menuItem);
}
