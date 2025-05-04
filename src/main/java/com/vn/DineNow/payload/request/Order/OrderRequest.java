package com.vn.DineNow.payload.request.Order;

import com.vn.DineNow.payload.request.orderItem.OrderItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Validated
public class OrderRequest {

    @Future(message = "Reservation time must be in the future")
    @NotNull(message = "Reservation time is required")
    OffsetDateTime reservationTime;

    @NotNull(message = "Number of people is required")
    @Min(value = 1, message = "There must be at least one person")
    Integer numberOfPeople;

    @NotNull(message = "Number of children is required")
    @Min(value = 0, message = "Number of children cannot be negative")
    Integer numberOfChild;

    @NotNull(message = "Phone number must not be null")
    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone must be 10 digits and start with 0")
    String numberPhone;

    @NotEmpty(message = "Order items must not be empty")
    List<@Valid OrderItemRequest> orderItems;
}
