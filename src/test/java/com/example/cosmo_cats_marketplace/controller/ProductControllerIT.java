package com.example.cosmo_cats_marketplace.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cosmo_cats_marketplace.config.MappersTestConfiguration;
import com.example.cosmo_cats_marketplace.domain.Category;
import com.example.cosmo_cats_marketplace.domain.Customer;
import com.example.cosmo_cats_marketplace.domain.Product;
import com.example.cosmo_cats_marketplace.dto.Product.ProductDto;
import com.example.cosmo_cats_marketplace.exception.GlobalExceptionHandler;
import com.example.cosmo_cats_marketplace.service.ProductService;
import com.example.cosmo_cats_marketplace.exception.service.ProductNotFoundException;
import com.example.cosmo_cats_marketplace.exception.service.ProductsNotFoundException;
import com.example.cosmo_cats_marketplace.mapper.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@Import({MappersTestConfiguration.class, GlobalExceptionHandler.class})
@DisplayName("Product Controller IT")
public class ProductControllerIT {

  @MockBean
  private ProductService productService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProductMapper productMapper;

  private static final Category category = Category.builder().id(1L).name("Cosmic Gadgets").build();
  private static final Product product = Product.builder()
          .id(1L)
          .name("Nebula Laser Pointer")
          .description("High-powered laser pointer for cosmic pets.")
          .price(35.0)
          .manufacturer("Gadget Galaxy")
          .category(category)
          .build();


  private static Stream<Arguments> provideInvalidProductFields() {
    return Stream.of(
            Arguments.of("sh", "name", "Product name must be between 3 and 30 characters."),
            Arguments.of("a".repeat(31), "name", "Product name must be between 3 and 30 characters."),
            Arguments.of("", "description", "Product description is required."),
            Arguments.of("a".repeat(301), "description", "Product description must be between 10 and 300 characters."),
            Arguments.of(-1.0, "price", "Product price must be greater than zero."),  // Double
            Arguments.of(10001.0, "price", "Product price cannot exceed 10000.")    // Double
    );
  }



  @ParameterizedTest
  @MethodSource("provideInvalidProductFields")
  void shouldThrowValidationErrors(Object invalidValue, String invalidField, String errorMsg) throws Exception {
    Product invalidProduct = switch (invalidField) {
      case "name" -> product.toBuilder().name((String) invalidValue).build();
      case "description" -> product.toBuilder().description((String) invalidValue).build();
      case "price" -> product.toBuilder().price((Double) invalidValue).build();
      default -> throw new IllegalArgumentException("Invalid field: " + invalidField);
    };

    ProductDto productDto = productMapper.toProductDto(invalidProduct);

    mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(productDto)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.invalidParams[0].parameterName").value(invalidField))
            .andExpect(jsonPath("$.invalidParams[0].errorMessage").value(errorMsg));
  }


  @Test
  void shouldReturnAllProducts() throws Exception {
    when(productService.getAllProducts()).thenReturn(Stream.of(product).toList());

    mockMvc.perform(get("/api/v1/products"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.products[0].name").value(product.getName()))
            .andExpect(jsonPath("$.products[0].description").value(product.getDescription()))
            .andExpect(jsonPath("$.products[0].price").value(product.getPrice()))
            .andExpect(jsonPath("$.products[0].category").value(product.getCategory().getName()));
  }

  @Test
  void shouldReturnProductById() throws Exception {
    when(productService.getProductById(1L)).thenReturn(product);

    mockMvc.perform(get("/api/v1/products/{productId}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Nebula Laser Pointer"))
            .andExpect(jsonPath("$.description").value("High-powered laser pointer for cosmic pets."))
            .andExpect(jsonPath("$.price").value(35.0))
            .andExpect(jsonPath("$.manufacturer").value("Gadget Galaxy"))
            .andExpect(jsonPath("$.category.id").value(1))
            .andExpect(jsonPath("$.category.name").value("Cosmic Gadgets"));
  }


  @Test
  void shouldCreateProduct() throws Exception {
    when(productService.createProduct(product)).thenReturn(product.getId());

    ProductDto productDto = productMapper.toProductDto(product);

    mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(productDto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
  }

  @Test
  void shouldDeleteProduct() throws Exception {
    mockMvc.perform(delete("/api/v1/products/{id}", product.getId()))
            .andDo(print())
            .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundForInvalidId() throws Exception {
    when(productService.getProductById(999L)).thenThrow(new ProductNotFoundException(999L));

    mockMvc.perform(get("/api/v1/products/{id}", 999L))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("product-not-found"));
  }
}
