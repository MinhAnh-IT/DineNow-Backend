package com.vn.DineNow.payload.request.restaurant;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class RestaurantUpdateDTO {
    private String name;

    private String address;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone must be 10 digits and start with 0")
    private String phone;

    private String description;

    private Long typeId;

    private List<MultipartFile> images;
}
