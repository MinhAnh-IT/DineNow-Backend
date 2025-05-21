package com.vn.DineNow.payload.response.payment;

import com.vn.DineNow.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class PaymentResponse {
    Long id;

    BigDecimal amount;

    String transactionId;

    @Enumerated(
            value = EnumType.STRING
    )
    PaymentStatus status;

    OffsetDateTime createdAt;

    OffsetDateTime updatedAt;
}
