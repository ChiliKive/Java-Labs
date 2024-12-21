package com.example.cosmo_cats_marketplace.controller;

import com.example.cosmo_cats_marketplace.AbstractTestcontainers;
import com.example.cosmo_cats_marketplace.dto.Product.ProductDto;
import com.example.cosmo_cats_marketplace.dto.Product.ProductListDto;
import com.example.cosmo_cats_marketplace.entity.CategoryEntity;
import com.example.cosmo_cats_marketplace.entity.ProductEntity;
import com.example.cosmo_cats_marketplace.repository.CategoryRepository;
import com.example.cosmo_cats_marketplace.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Product Controller IT")
class ProductControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testCreateProduct() throws Exception {
    CategoryEntity category = categoryRepository.save(
            CategoryEntity.builder()
                    .name("Cosmic Category")
                    .build()
    );

    ProductDto productDto = ProductDto.builder()
            .name("Cosmic universe Toy")
            .description("A toy for universe adventures")
            .price(99.99)
            .manufacturer("Galaxy Inc.")
            .category(com.example.cosmo_cats_marketplace.dto.Category.CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build())
            .build();

    mockMvc.perform(post("/api/v1/products")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDto)))
            .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void testGetAllProducts() throws Exception {
    CategoryEntity category = categoryRepository.save(
            CategoryEntity.builder()
                    .name("Test Category")
                    .build()
    );

    productRepository.saveAll(List.of(
            ProductEntity.builder()
                    .name("Product 1")
                    .description("Description 1")
                    .price(50.0)
                    .manufacturer("Manufacturer 1")
                    .category(category)
                    .build(),
            ProductEntity.builder()
                    .name("Product 2")
                    .description("Description 2")
                    .price(150.0)
                    .manufacturer("Manufacturer 2")
                    .category(category)
                    .build()
    ));

    mockMvc.perform(get("/api/v1/products")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.products.length()").value(2));
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void testGetProductById() throws Exception {
    CategoryEntity category = categoryRepository.save(
            CategoryEntity.builder()
                    .name("Test Category")
                    .build()
    );

    ProductEntity product = productRepository.save(
            ProductEntity.builder()
                    .name("Test Product")
                    .description("Description")
                    .price(100.0)
                    .manufacturer("Manufacturer")
                    .category(category)
                    .build()
    );

    mockMvc.perform(get("/api/v1/products/{id}", product.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Product"));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testUpdateProduct() throws Exception {
    CategoryEntity category = categoryRepository.save(
            CategoryEntity.builder()
                    .name("Test Category")
                    .build()
    );

    ProductEntity product = productRepository.save(
            ProductEntity.builder()
                    .name("Old Product")
                    .description("Old Description")
                    .price(100.0)
                    .manufacturer("Old Manufacturer")
                    .category(category)
                    .build()
    );

    ProductDto updatedProductDto = ProductDto.builder()
            .name("Updated Product")
            .description("Updated Description")
            .price(200.0)
            .manufacturer("Updated Manufacturer")
            .category(com.example.cosmo_cats_marketplace.dto.Category.CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build())
            .build();

    mockMvc.perform(put("/api/v1/products/{id}", product.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedProductDto)))
            .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testDeleteProduct() throws Exception {
    CategoryEntity category = categoryRepository.save(
            CategoryEntity.builder()
                    .name("Test Category")
                    .build()
    );

    ProductEntity product = productRepository.save(
            ProductEntity.builder()
                    .name("Test Product")
                    .description("Description")
                    .price(100.0)
                    .manufacturer("Manufacturer")
                    .category(category)
                    .build()
    );

    mockMvc.perform(delete("/api/v1/products/{id}", product.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

    assertFalse(productRepository.existsById(product.getId()));
  }
}
