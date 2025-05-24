package com.vn.DineNow.payload.request.restaurant;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantUpdateDTO {
    String name;

    String address;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone must be 10 digits and start with 0")
    String phone;

    String description;

    Long typeId;

    List<MultipartFile> images;
}
