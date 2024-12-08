package com.example.cosmo_cats_marketplace.exception.service;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
  private static final String MESSAGE_TEMPLATE = "Product with ID '%d' was not found in the catalog";

  public ProductNotFoundException(Long productId) {
    super(String.format(MESSAGE_TEMPLATE, productId));
  }
}