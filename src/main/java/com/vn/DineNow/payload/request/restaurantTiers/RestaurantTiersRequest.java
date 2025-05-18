package com.vn.DineNow.payload.request.restaurantTiers;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTiersRequest {

    @NotBlank(message = "Tier name must not be blank")
    String name;

    @NotNull(message = "Fee per guest is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Fee per guest must be greater than 0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal feePerGuest;

    @NotBlank(message = "Description must not be blank")
    String description;
}
