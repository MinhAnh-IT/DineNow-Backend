package com.vn.DineNow.entities;

import com.vn.DineNow.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;


@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    double latitude;

    @Column(nullable = false)
    double longitude;

    @Column(nullable = false, columnDefinition = "text")
    String address;

    @Column(nullable = false, length = 20)
    String phone;

    @Column(columnDefinition = "text")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    RestaurantType type;

    @Column
    double averageRating;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RestaurantStatus status = RestaurantStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id", nullable = false)
    RestaurantTiers restaurantTier;

    @Column
    @CreationTimestamp
    OffsetDateTime createdAt;

    @Column
    @UpdateTimestamp
    OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @OneToMany(mappedBy = "restaurant")
    Set<RestaurantImage> restaurantRestaurantImages;

    @OneToMany(mappedBy = "restaurant")
    Set<Reservation> restaurantReservations;

    @OneToMany(mappedBy = "restaurant")
    Set<MenuItem> restaurantMenuItems;

    @OneToMany(mappedBy = "restaurant")
    Set<Review> restaurantReviews;

    @OneToMany(mappedBy = "restaurant")
    Set<FavoriteRestaurant> restaurantFavoriteRestaurants;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<FoodCategory> foodCategories;
}
