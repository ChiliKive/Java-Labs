package com.example.cosmo_cats_marketplace.dto.product;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ProductEntry {
  UUID id;
  String name;
  String description;
  Double price;
  String manufacturer;
  String category;
}
