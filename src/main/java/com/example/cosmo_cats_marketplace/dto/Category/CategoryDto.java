package com.example.cosmo_cats_marketplace.dto.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CategoryDto {
  Long id;

  @NotBlank(message = "Category name is required.")
  @Size(min = 3, max = 30, message = "Category name must be between 3 and 30 characters.")
  String name;
}
