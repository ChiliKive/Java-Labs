package com.example.cosmo_cats_marketplace.service;

import java.util.List;
import java.util.UUID;

import com.example.cosmo_cats_marketplace.domain.Product;

public interface ProductService {
    UUID createProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(UUID id);
    void updateProduct(Product product);
    void deleteProductById(UUID productId);
}
