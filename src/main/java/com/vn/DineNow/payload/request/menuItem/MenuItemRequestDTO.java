package com.vn.DineNow.payload.request.menuItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemRequestDTO {

    @NotNull(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Digits(integer = 12, fraction = 2, message = "Price must be a valid decimal number with up to 12 digits and 2 decimal places")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    @Builder.Default
    private Boolean available = true;

    @NotNull(message = "Category ID is required")
    private Long category;

    @NotNull(message = "Image file is required")
    private MultipartFile imageUrl;
}
