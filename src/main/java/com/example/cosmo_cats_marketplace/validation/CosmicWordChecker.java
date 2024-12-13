package com.example.cosmo_cats_marketplace.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;

public class CosmicWordChecker implements ConstraintValidator<CosmicWordCheck, String> {

  private static final Set<String> COSMIC_WORDS = Set.of("universe", "nebula", "planet", "astronaut", "stellar", "zero-gravity");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) {
      return false;
    }
    String normalizedValue = value.toLowerCase();
    return COSMIC_WORDS.stream().anyMatch(normalizedValue::contains);
  }
}
