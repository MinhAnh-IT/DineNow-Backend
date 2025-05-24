package com.vn.DineNow.entities;

import com.vn.DineNow.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Setter
@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal amount;

    String transactionId;

    @Column
    @Enumerated(EnumType.STRING)
    PaymentStatus status = PaymentStatus.PENDING;

    @Column
    @CreationTimestamp
    OffsetDateTime createdAt;

    @Column
    @UpdateTimestamp
    OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

}
