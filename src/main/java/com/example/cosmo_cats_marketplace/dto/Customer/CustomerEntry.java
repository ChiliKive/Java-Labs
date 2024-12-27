package com.example.cosmo_cats_marketplace.dto.customer;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class CustomerEntry {
  UUID id;
  String name;
  String address;
  String phone;
  String email;
}
