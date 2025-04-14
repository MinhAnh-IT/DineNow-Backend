package com.vn.DineNow.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RestaurantEnabledValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRestaurantEnabled {
    String message() default "The restaurant has been disabled or is currently unavailable";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
