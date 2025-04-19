package com.vn.DineNow.payload.request.menuItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuItemUpdateDTO {
    private String name;

    private String description;

    @Digits(integer = 12, fraction = 2, message = "Price must be a valid decimal number with up to 12 digits and 2 decimal places")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    private Boolean available;

    private MultipartFile imageUrl;
}
