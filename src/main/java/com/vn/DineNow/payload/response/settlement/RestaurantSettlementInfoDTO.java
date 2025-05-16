package com.vn.DineNow.payload.response.settlement;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantSettlementInfoDTO {
    Long restaurantId;
    String restaurantName;
    String address;
    String phoneNumber;
}
