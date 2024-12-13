package com.example.cosmo_cats_marketplace.dto.Product;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ProductEntry {
  Long id;
  String name;
  String description;
  Double price;
  String manufacturer;
  String category;
}
