package com.example.cosmo_cats_marketplace.domain;

import java.util.UUID;
import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Order {
    UUID id;
    Customer customer;
    String status;
    List<Product> products;
}
