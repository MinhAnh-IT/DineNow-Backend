package com.vn.DineNow.payload.request.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantRequestDTO {

    @NotBlank(message = "Restaurant name must not be blank")
    String name;

    @NotBlank(message = "Address must not be blank")
    String address;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone must be 10 digits and start with 0")
    String phone;

    @NotNull(message = "Restaurant description must not be blank")
    String description;

    @NotNull(message = "Restaurant type is required")
    Long typeId;

    @NotNull(message = "Restaurant tier is required")
    Long restaurantTierId;

    @NotNull(message = "images is required")
    List<MultipartFile> images;
}
