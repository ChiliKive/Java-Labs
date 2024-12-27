package com.example.cosmo_cats_marketplace.controller;

import com.example.cosmo_cats_marketplace.dto.Product.ProductDto;
import com.example.cosmo_cats_marketplace.dto.Product.ProductListDto;
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
  public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
    ProductDto product = productMapper.toProductDto(productService.getProductById(productId));
    return ResponseEntity.ok(product);
  }

  @PostMapping
  public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductDto productDto) {
    Long productId = productService.createProduct(productMapper.toProduct(productDto));
    URI location = URI.create(String.format("/api/v1/products/%d", productId));
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(location);
    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @PutMapping("/{productId}")
  public ResponseEntity<Void> updateProduct(
          @PathVariable Long productId,
          @RequestBody ProductDto productDto) {
    productService.updateProduct(productMapper.toProduct(productDto).toBuilder().id(productId).build());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<Void> removeProduct(@PathVariable Long productId) {
    productService.deleteProductById(productId);
    return ResponseEntity.noContent().build();
  }
}
