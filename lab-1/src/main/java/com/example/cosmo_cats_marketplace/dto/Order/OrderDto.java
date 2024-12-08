package com.example.cosmo_cats_marketplace.dto.Order;

import com.example.cosmo_cats_marketplace.dto.Product.ProductListDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class OrderDto {
  Long id;

  String status;

  @NotNull(message = "Products list cannot be null.")
  ProductListDto products;
}
