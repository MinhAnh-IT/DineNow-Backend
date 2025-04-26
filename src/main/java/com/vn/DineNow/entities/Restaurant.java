package com.vn.DineNow.entities;

import com.vn.DineNow.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private RestaurantType type;

    @Column(precision = 2, scale = 1)
    private BigDecimal averageRating = BigDecimal.valueOf(0.0);;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status = RestaurantStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id", nullable = false)
    private RestaurantTiers restaurantTier;

    @Column
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "restaurant")
    private Set<RestaurantImage> restaurantRestaurantImages;

    @OneToMany(mappedBy = "restaurant")
    private Set<Reservation> restaurantReservations;

    @OneToMany(mappedBy = "restaurant")
    private Set<MenuItem> restaurantMenuItems;

    @OneToMany(mappedBy = "restaurant")
    private Set<Review> restaurantReviews;

    @OneToMany(mappedBy = "restaurant")
    private Set<FavoriteRestaurant> restaurantFavoriteRestaurants;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FoodCategory> foodCategories;
}
