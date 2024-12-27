package com.example.cosmo_cats_marketplace.dto.order;

import com.example.cosmo_cats_marketplace.dto.product.ProductListDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class OrderDto {
  UUID id;

  String status;

  @NotNull(message = "Products list cannot be null.")
  ProductListDto products;
}
