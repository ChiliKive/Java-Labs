package com.example.cosmo_cats_marketplace.domain;

import lombok.Builder;
import lombok.Value;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Product {
    UUID id;
    String name;
    String description;
    Double price;
    String manufacturer;
    Category category;
}
