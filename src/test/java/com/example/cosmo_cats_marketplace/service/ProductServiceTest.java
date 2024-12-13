package com.example.cosmo_cats_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.cosmo_cats_marketplace.domain.Category;
import com.example.cosmo_cats_marketplace.domain.Product;
import com.example.cosmo_cats_marketplace.exception.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DisplayName("Enhanced Product Service Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    private static Long testProductId;
    private static final String TEST_PRODUCT_NAME = "Zero-Gravity Toy";

    @Test
    @Order(1)
    void shouldReturnAllProducts() {
        List<Product> products = productService.getAllProducts();
        assertNotNull(products, "The product list should not be null.");
        assertEquals(3, products.size(), "The size of the product list does not match the expected value.");
    }

    @Test
    @Order(2)
    void shouldThrowProductsNotFoundExceptionIfNoProductsExist() {
        List<Product> products = new ArrayList<>(productService.getAllProducts());
        products.forEach(product -> productService.deleteProductById(product.getId()));

        assertThrows(ProductsNotFoundException.class, productService::getAllProducts);
    }

    @Test
    @Order(3)
    void shouldCreateProduct() {
        Product newProduct = Product.builder()
                .name(TEST_PRODUCT_NAME)
                .description("A toy designed for zero-gravity conditions.")
                .price(100.0)
                .manufacturer("SpaceToys Inc.")
                .category(Category.builder().id(2L).name("Cosmic Gadgets").build())
                .build();

        testProductId = productService.createProduct(newProduct);
        Product createdProduct = productService.getProductById(testProductId);

        assertNotNull(createdProduct, "Created product should not be null.");
        assertEquals(newProduct.getName(), createdProduct.getName(), "Product names do not match.");
        assertEquals(newProduct.getDescription(), createdProduct.getDescription(), "Product descriptions do not match.");
        assertEquals(newProduct.getPrice(), createdProduct.getPrice(), "Product prices do not match.");
        assertEquals(newProduct.getManufacturer(), createdProduct.getManufacturer(), "Product manufacturers do not match.");
        assertEquals(newProduct.getCategory(), createdProduct.getCategory(), "Product categories do not match.");
    }

    @Test
    @Order(4)
    void shouldThrowProductAlreadyExistsException() {
        Product duplicateProduct = Product.builder()
                .name(TEST_PRODUCT_NAME)
                .description("Duplicate description.")
                .price(120.0)
                .manufacturer("Duplicate Manufacturer")
                .category(Category.builder().id(2L).name("Cosmic Gadgets").build())
                .build();

        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(duplicateProduct));
    }

    @Test
    @Order(5)
    void shouldUpdateProduct() {
        Product updatedProduct = Product.builder()
                .id(testProductId)
                .name("Updated Zero-Gravity Toy")
                .description("An updated description for zero-gravity conditions.")
                .price(120.0)
                .manufacturer("SpaceToys Updated Inc.")
                .category(Category.builder().id(2L).name("Cosmic Gadgets").build())
                .build();

        productService.updateProduct(updatedProduct);
        Product retrievedProduct = productService.getProductById(testProductId);

        assertNotNull(retrievedProduct, "Updated product should not be null.");
        assertEquals(updatedProduct.getName(), retrievedProduct.getName(), "Updated product name does not match.");
        assertEquals(updatedProduct.getDescription(), retrievedProduct.getDescription(), "Updated product description does not match.");
        assertEquals(updatedProduct.getPrice(), retrievedProduct.getPrice(), "Updated product price does not match.");
        assertEquals(updatedProduct.getManufacturer(), retrievedProduct.getManufacturer(), "Updated product manufacturer does not match.");
        assertEquals(updatedProduct.getCategory(), retrievedProduct.getCategory(), "Updated product category does not match.");
    }

    @Test
    @Order(6)
    void shouldThrowProductNotFoundExceptionOnUpdate() {
        Product nonExistentProduct = Product.builder()
                .id(999L)
                .name("Non-existent Product")
                .description("This product does not exist.")
                .price(50.0)
                .manufacturer("Unknown")
                .category(Category.builder().id(3L).name("Other").build())
                .build();

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(nonExistentProduct));
    }

    @Test
    @Order(7)
    void shouldDeleteProduct() {
        productService.deleteProductById(testProductId);
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(testProductId));
    }

    @Test
    @Order(8)
    void shouldThrowProductNotFoundExceptionForInvalidId() {
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));
    }
}
