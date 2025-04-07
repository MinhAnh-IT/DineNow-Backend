package com.vn.DineNow.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;


@Getter
@Setter
@Entity
public class Restaurant {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(columnDefinition = "text")
    private String description;

    @Column(precision = 5, scale = 2)
    private BigDecimal averageRating;

    @Column
    private String status;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "restaurant")
    private Set<RestaurantImage> restaurantRestaurantImages;

    @OneToMany(mappedBy = "restaurant")
    private Set<DiningTable> restaurantDiningTables;

    @OneToMany(mappedBy = "restaurant")
    private Set<Reservation> restaurantReservations;

    @OneToMany(mappedBy = "restaurant")
    private Set<MenuItem> restaurantMenuItems;

    @OneToMany(mappedBy = "restaurant")
    private Set<Review> restaurantReviews;

    @OneToMany(mappedBy = "restaurant")
    private Set<FavoriteRestaurant> restaurantFavoriteRestaurants;

}
