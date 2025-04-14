package com.vn.DineNow.validation;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.repositories.RestaurantRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantEnabledValidator implements ConstraintValidator<ValidRestaurantEnabled, Long> {

    private final RestaurantRepository restaurantRepository;

    @Override
    public boolean isValid(Long restaurantId, ConstraintValidatorContext context) {
        if (restaurantId == null) return false;

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null || Boolean.FALSE.equals(restaurant.getEnabled())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(StatusCode.RESTAURANT_DISABLED.getMessage())
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
