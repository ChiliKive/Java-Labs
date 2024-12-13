package com.example.cosmo_cats_marketplace.validation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CosmicWordChecker.class)
public @interface CosmicWordCheck {
    String message() default "The value must contain one of the cosmic words: universe, nebula, planet, astronaut, stellar, zero-gravity";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
