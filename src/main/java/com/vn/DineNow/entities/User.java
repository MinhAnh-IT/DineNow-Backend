package com.vn.DineNow.entities;

import com.vn.DineNow.enums.SignWith;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Set;


@Setter
@Getter
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column
    private String password;

    @Column(length = 10)
    private String phone;

    @Column(nullable = false)
    private String role;

    @Column
    private Boolean isVerified;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column
    @Enumerated(EnumType.STRING)
    private SignWith provider = SignWith.LOCAL;

    @Column(name = "provider_id")
    private String providerId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "owner")
    private Set<Restaurant> ownerRestaurants;

    @OneToMany(mappedBy = "customer")
    private Set<Reservation> customerReservations;

    @OneToMany(mappedBy = "customer")
    private Set<Review> customerReviews;

    @OneToMany(mappedBy = "user")
    private Set<FavoriteRestaurant> userFavoriteRestaurants;
}
