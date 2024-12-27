package com.example.cosmo_cats_marketplace.exception.service;

public class ProductsNotFoundException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "No products are available in the catalog";

    public ProductsNotFoundException() {
        super(MESSAGE_TEMPLATE);
    }
}
