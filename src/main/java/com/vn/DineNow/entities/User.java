package com.vn.DineNow.entities;

import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.SignWith;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Set;


@Setter
@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String fullName;

    @Column(nullable = false)
    String email;

    @Column
    String password;

    @Column(length = 10)
    String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Role role;

    @Column
    Boolean isVerified;

    @Column(nullable = false)
    Boolean enabled = true;

    @Column
    @Enumerated(EnumType.STRING)
    SignWith provider = SignWith.LOCAL;

    @Column(name = "provider_id")
    String providerId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    OffsetDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    BankAccount bankAccount;

    @UpdateTimestamp
    @Column(nullable = false)
    OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "owner")
    Set<Restaurant> ownerRestaurants;

    @OneToMany(mappedBy = "customer")
    Set<Reservation> customerReservations;

    @OneToMany(mappedBy = "customer")
    Set<Review> customerReviews;

    @OneToMany(mappedBy = "user")
    Set<FavoriteRestaurant> userFavoriteRestaurants;
}
