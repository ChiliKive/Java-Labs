package com.example.cosmo_cats_marketplace.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Category {
    Long id;
    String name;
}
