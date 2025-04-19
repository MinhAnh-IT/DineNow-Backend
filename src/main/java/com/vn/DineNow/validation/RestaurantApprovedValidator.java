package com.vn.DineNow.validation;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.repositories.RestaurantRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantApprovedValidator implements ConstraintValidator<ValidRestaurantApprovedValidator, Long> {

    private final RestaurantRepository restaurantRepository;

    @Override
    public boolean isValid(Long restaurantId, ConstraintValidatorContext context) {
        if (restaurantId == null) return false;

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

        if (restaurant == null || restaurant.getStatus() != RestaurantStatus.APPROVED) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(StatusCode.RESTAURANT_NOT_APPROVED.getMessage())
                    .addConstraintViolation();
            return false;
        }

        return true;
    }


}
