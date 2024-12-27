package com.example.cosmo_cats_marketplace.dto.product;

import com.example.cosmo_cats_marketplace.dto.category.CategoryDto;
import com.example.cosmo_cats_marketplace.validation.CosmicWordCheck;
import com.example.cosmo_cats_marketplace.validation.ExtendedValidation;
import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
@Jacksonized
@GroupSequence({ProductDto.class, ExtendedValidation.class})
public class ProductDto {
  UUID id;

  @NotBlank(message = "Product name is required.")
  @Size(min = 3, max = 70, message = "Product name must be between 3 and 70 characters.")
  @CosmicWordCheck(groups = ExtendedValidation.class)
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
