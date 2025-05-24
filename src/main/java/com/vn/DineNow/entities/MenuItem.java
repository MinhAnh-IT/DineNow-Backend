package com.vn.DineNow.entities;

import jakarta.persistence.*;
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
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(columnDefinition = "text")
    String description;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal price;

    @Column
    Boolean available;

    @Column
    String imageUrl;

    @Column
    private Double averageRating;

    @Column
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menuItem")
    private Set<OrderItem> menuItemOrderItems;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private FoodCategory category;
}
