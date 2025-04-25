package com.vn.DineNow.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "restaurant_tiers")
@Getter
@Setter
public class RestaurantTiers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column
    String name;

    BigDecimal feePerGuest;

    @Column
    String description;

    @OneToMany(mappedBy = "restaurantTier")
    List<Restaurant> restaurants;
}
