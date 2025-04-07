package com.vn.DineNow.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Getter
@Setter
public class PaymentDTO {

    private Long id;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal amount;

    @NotNull
    @Size(max = 255)
    private String paymentMethod;

    @NotNull
    @Size(max = 100)
    private String transactionId;

    @Size(max = 255)
    private String status;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    @NotNull
    private Long reservation;

}
