package com.vn.DineNow.payload.request.restaurantType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTypeRequest {

    @NotBlank(message = "Type name must not be blank")
    String name;

    @NotBlank(message = "Description must not be blank")
    String description;

    @NotNull(message = "Image is required")
    MultipartFile imageUrl;
}
