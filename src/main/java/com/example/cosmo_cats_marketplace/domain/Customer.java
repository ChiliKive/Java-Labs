package com.example.cosmo_cats_marketplace.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Customer {
    UUID id;
    String name;
    String address;
    String phone;
    String email;
}
