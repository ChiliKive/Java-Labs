package com.example.cosmo_cats_marketplace.dto.Customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CustomerDto {

  @NotBlank(message = "Please provide a valid customer name.")
  @Size(min = 3, max = 30, message = "Customer name must be between 3 and 30 characters.")
  String name;

  @NotBlank(message = "Please provide a valid customer address.")
  @Size(min = 3, max = 50, message = "Customer address must be between 3 and 50 characters.")
  String address;

  @NotBlank(message = "A phone number is required.")
  @Pattern(
          regexp = "^\\+?[0-9]{10,15}$",
          message = "Phone number must follow the standard format."
  )
  String phone;

  @NotBlank(message = "An email address is required.")
  @Email(message = "Please provide a valid email address.")
  String email;
}
