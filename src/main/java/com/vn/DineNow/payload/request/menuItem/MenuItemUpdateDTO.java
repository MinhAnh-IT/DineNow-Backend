package com.vn.DineNow.payload.request.menuItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItemUpdateDTO {
    String name;

    String description;

    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal price;

    Boolean available;

    Long foodCategoryId;

    MultipartFile imageUrl;
}
