package com.example.cosmo_cats_marketplace.controller;

import com.example.cosmo_cats_marketplace.AbstractIt;
import com.example.cosmo_cats_marketplace.config.MappersTestConfiguration;
import com.example.cosmo_cats_marketplace.dto.category.CategoryDto;
import com.example.cosmo_cats_marketplace.dto.product.ProductDto;
import com.example.cosmo_cats_marketplace.mapper.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DisplayName("Product Controller IT")
@Tag("product-service")
@Import({MappersTestConfiguration.class})
public class ProductControllerIT extends AbstractIt {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ProductMapper productMapper;

  private static final UUID CATEGORY_ID = UUID.fromString("db084cd0-a00a-40af-8f8b-450a7da69d0c");
  private static final String CATEGORY_NAME = "Cosmic Gadgets";

  private static final UUID PRODUCT_ID = UUID.fromString("eca8be53-a619-4490-b88a-5d1e248a687c");
  private static final String PRODUCT_NAME = "Nebula Laser Pointer";
  private static final String PRODUCT_DESCRIPTION = "High-powered laser pointer for cosmic pets.";
  private static final double PRODUCT_PRICE = 35.0;
  private static final String PRODUCT_MANUFACTURER = "Gadget Galaxy";

  private static final CategoryDto CATEGORY_DTO = CategoryDto.builder()
          .id(CATEGORY_ID)
          .name(CATEGORY_NAME)
          .build();

  private static final ProductDto TEST_PRODUCT = ProductDto.builder()
          .id(PRODUCT_ID)
          .name(PRODUCT_NAME)
          .description(PRODUCT_DESCRIPTION)
          .price(PRODUCT_PRICE)
          .manufacturer(PRODUCT_MANUFACTURER)
          .category(CATEGORY_DTO)
          .build();

  @Test
  @DisplayName("Should fetch all products successfully")
  void shouldGetAllProducts() throws Exception {
    mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.products").isArray())
            .andExpect(jsonPath("$.products.length()").isNotEmpty());
  }

  @Test
  @DisplayName("Should fetch a single product by ID successfully")
  void shouldGetProductById() throws Exception {
    ProductDto productDto = createTestProduct();
    String expectedName = productDto.getName();

    mockMvc.perform(get("/api/v1/products/{id}", productDto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(expectedName))
            .andExpect(jsonPath("$.description").value(PRODUCT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(PRODUCT_PRICE))
            .andExpect(jsonPath("$.manufacturer").value(PRODUCT_MANUFACTURER))
            .andExpect(jsonPath("$.category.id").value(CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.category.name").value(CATEGORY_NAME));
  }

  @Test
  @DisplayName("Should create a new product successfully")
  void shouldCreateProduct() throws Exception {
    String productJson = objectMapper.writeValueAsString(TEST_PRODUCT);

    mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
            .andExpect(jsonPath("$.description").value(PRODUCT_DESCRIPTION));
  }

  @Test
  @DisplayName("Should handle validation error when creating a product")
  void shouldFailToCreateProductWithInvalidData() throws Exception {
    String invalidProductJson = """
        {
            "name": "T",
            "description": "",
            "price": -10.0,
            "manufacturer": "Gadget Galaxy",
            "category": {
                "id": "db084cd0-a00a-40af-8f8b-450a7da69d0c",
                "name": "Cosmic Gadgets"
            }
        }
        """;

    mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidProductJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation Error"))
            .andExpect(jsonPath("$.invalidParams").isArray());
  }

  @ParameterizedTest
  @MethodSource("provideInvalidProductFields")
  void shouldThrowValidationErrors(Object invalidValue, String invalidField, String errorMsg) throws Exception {
    ProductDto invalidProduct = switch (invalidField) {
      case "name" -> TEST_PRODUCT.toBuilder().name((String) invalidValue).build();
      case "description" -> TEST_PRODUCT.toBuilder().description((String) invalidValue).build();
      case "price" -> TEST_PRODUCT.toBuilder().price((Double) invalidValue).build();
      default -> throw new IllegalArgumentException("Invalid field: " + invalidField);
    };

    mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidProduct)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.invalidParams[0].parameterName").value(invalidField))
            .andExpect(jsonPath("$.invalidParams[0].errorMessage").value(errorMsg));
  }

  private static Stream<Arguments> provideInvalidProductFields() {
    return Stream.of(
            Arguments.of("sh", "name", "Product name must be between 3 and 70 characters."),
            Arguments.of("a".repeat(71), "name", "Product name must be between 3 and 70 characters."),
            Arguments.of("", "description", "Product description is required."),
            Arguments.of("a".repeat(301), "description", "Product description must be between 10 and 300 characters."),
            Arguments.of(-1.0, "price", "Product price must be greater than zero."),
            Arguments.of(10001.0, "price", "Product price cannot exceed 10000.")
    );
  }

  private ProductDto createTestProduct() throws Exception {
    ProductDto productDto = ProductDto.builder()
            .name(PRODUCT_NAME + "-" + UUID.randomUUID())
            .description(PRODUCT_DESCRIPTION)
            .price(PRODUCT_PRICE)
            .manufacturer(PRODUCT_MANUFACTURER)
            .category(CATEGORY_DTO)
            .build();

    String productJson = objectMapper.writeValueAsString(productDto);

    String responseJson = mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productJson))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    UUID productId = UUID.fromString(objectMapper.readTree(responseJson).get("id").asText());
    return productDto.toBuilder().id(productId).build();
  }

}
