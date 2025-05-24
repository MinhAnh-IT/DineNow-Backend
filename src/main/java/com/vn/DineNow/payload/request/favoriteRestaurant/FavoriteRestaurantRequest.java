package com.vn.DineNow.payload.request.favoriteRestaurant;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;


@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteRestaurantRequest {

    Long id;

    @Builder.Default
    OffsetDateTime createdAt = OffsetDateTime.now();

    @NotNull
    Long user;

    @NotNull
    Long restaurant;

}
