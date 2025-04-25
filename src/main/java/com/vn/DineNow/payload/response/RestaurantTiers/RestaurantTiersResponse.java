package com.vn.DineNow.payload.response.RestaurantTiers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantTiersResponse {

    long id;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal feePerGuest;

    private String description;
}
