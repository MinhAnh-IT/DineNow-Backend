package com.vn.DineNow.payload.request.Order;

import com.vn.DineNow.payload.request.OrderItem.OrderItemRequest;
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

    @NotNull(message = "Password must not be null")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character and be at least 8 characters long"
    )
    String numberPhone;

    @NotEmpty(message = "Order items must not be empty")
    List<@Valid OrderItemRequest> orderItems;
}
