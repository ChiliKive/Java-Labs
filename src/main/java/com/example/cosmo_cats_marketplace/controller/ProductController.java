package com.example.cosmo_cats_marketplace.controller;

import com.example.cosmo_cats_marketplace.dto.product.ProductDto;
import com.example.cosmo_cats_marketplace.dto.product.ProductListDto;
import com.example.cosmo_cats_marketplace.mapper.ProductMapper;
import com.example.cosmo_cats_marketplace.service.ProductService;
import com.example.cosmo_cats_marketplace.validation.ExtendedValidation;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@Validated(ExtendedValidation.class)
@RequestMapping("/api/v1/products")
public class ProductController {
  private final ProductService productService;
  private final ProductMapper productMapper;

  ProductController(ProductService productService, ProductMapper productMapper) {
    this.productService = productService;
    this.productMapper = productMapper;
  }

  @GetMapping
  public ResponseEntity<ProductListDto> getAllProducts() {
    ProductListDto products = productMapper.toProductListDto(productService.getAllProducts());
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductDto> getProductById(@PathVariable UUID productId) {
    ProductDto product = productMapper.toProductDto(productService.getProductById(productId));
    return ResponseEntity.ok(product);
  }

  @PostMapping
  public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto productDto) {
    UUID productId = productService.createProduct(productMapper.toProduct(productDto));
    ProductDto createdProduct = productMapper.toProductDto(productMapper.toProduct(productDto).toBuilder().id(productId).build());
    URI location = URI.create(String.format("/api/v1/products/%s", productId));
    return ResponseEntity.created(location).body(createdProduct);
  }

  @PutMapping("/{productId}")
  public ResponseEntity<Void> updateProduct(
      @PathVariable UUID productId,
      @RequestBody ProductDto productDto) {
    productService.updateProduct(productMapper.toProduct(productDto).toBuilder().id(productId).build());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<Void> removeProduct(@PathVariable UUID productId) {
    productService.deleteProductById(productId);
    return ResponseEntity.noContent().build();
  }
}
