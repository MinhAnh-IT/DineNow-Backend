package com.vn.DineNow.payload.request.orderItem;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemSimpleResponse {
    long menuItemId;
    String menuItemName;
    long menuItemPrice;
    BigDecimal totalPrice;
    int quantity;
    String menuItemImageUrl;

}
