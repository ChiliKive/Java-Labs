package com.example.cosmo_cats_marketplace.exception.service;

public class ProductAlreadyExistsException extends RuntimeException {
  private static final String MESSAGE_TEMPLATE = "A product named '%s' already exists in the catalog";

  public ProductAlreadyExistsException(String name) {
    super(String.format(MESSAGE_TEMPLATE, name));
  }
}
