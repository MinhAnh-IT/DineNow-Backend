package com.vn.DineNow.payload.request.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectOrderRequest {
    String reason;
}
