package com.vn.DineNow.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DiningTableDTO {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String tableNumber;

    @NotNull
    private Integer capacity;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    @NotNull
    private Long restaurant;

}
