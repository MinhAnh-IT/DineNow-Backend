package com.vn.DineNow.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "restaurant_payments")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    BigDecimal amount;

    @CreationTimestamp
    @Column(name = "payment_date", updatable = false)
    OffsetDateTime paymentDate;

    String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    Restaurant restaurant;
}
