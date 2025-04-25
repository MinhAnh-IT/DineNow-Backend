package com.vn.DineNow.payload.request.restaurantType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTypeRequest {
    String name;
    String description;
    MultipartFile imageUrl;
}
