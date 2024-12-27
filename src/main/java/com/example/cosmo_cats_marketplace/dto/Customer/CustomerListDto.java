package com.example.cosmo_cats_marketplace.dto.customer;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CustomerListDto {
  List<CustomerEntry> customers;
}
