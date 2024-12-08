package com.example.cosmo_cats_marketplace.dto.Product;

import com.example.cosmo_cats_marketplace.dto.Category.CategoryDto;
import com.example.cosmo_cats_marketplace.validation.CosmicWordCheck;
import com.example.cosmo_cats_marketplace.validation.ExtendedValidation;
import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@GroupSequence({ProductDto.class})
public class ProductDto {

  @NotBlank(message = "Product name is required.")
  @CosmicWordCheck(groups = ExtendedValidation.class)
  @Size(min = 3, max = 30, message = "Product name must be between 3 and 30 characters.")
  String name;

  @NotBlank(message = "Product description is required.")
  @Size(min = 10, max = 300, message = "Product description must be between 10 and 300 characters.")
  String description;

  @Positive(message = "Product price must be greater than zero.")
  @Max(value = 10000, message = "Product price cannot exceed 10000.")
  Double price;

  @NotBlank(message = "Manufacturer is required.")
  @Size(max = 50, message = "Manufacturer name cannot exceed 100 characters.")
  String manufacturer;

  @Valid
  @NotNull(message = "Category is required.")
  CategoryDto category;
}
