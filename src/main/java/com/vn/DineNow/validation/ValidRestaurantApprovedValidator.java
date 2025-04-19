package com.vn.DineNow.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RestaurantApprovedValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRestaurantApprovedValidator {
    String message() default "The restaurant is not approved or currently unavailable.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
