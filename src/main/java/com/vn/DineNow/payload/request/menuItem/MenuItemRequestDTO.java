package com.vn.DineNow.payload.request.menuItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItemRequestDTO {

    @NotNull(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    String name;

    String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Fee per guest must be greater than 0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal price;

    @Builder.Default
    Boolean available = true;

    @NotNull(message = "Category ID is required")
    Long category;

    @NotNull(message = "Image file is required")
    MultipartFile imageUrl;
}
