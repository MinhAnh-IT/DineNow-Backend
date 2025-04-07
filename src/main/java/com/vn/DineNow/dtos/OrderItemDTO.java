package com.vn.DineNow.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class OrderItemDTO {

    private Long id;

    @NotNull
    private Integer quantity;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    @NotNull
    private Long order;

    @NotNull
    private Long menuItem;

}
