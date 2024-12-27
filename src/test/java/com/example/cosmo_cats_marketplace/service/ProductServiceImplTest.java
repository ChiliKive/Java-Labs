package com.example.cosmo_cats_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.cosmo_cats_marketplace.domain.Category;
import com.example.cosmo_cats_marketplace.domain.Product;
import com.example.cosmo_cats_marketplace.entity.CategoryEntity;
import com.example.cosmo_cats_marketplace.entity.ProductEntity;
import com.example.cosmo_cats_marketplace.exception.service.ProductAlreadyExistsException;
import com.example.cosmo_cats_marketplace.exception.service.ProductNotFoundException;
import com.example.cosmo_cats_marketplace.repository.ProductRepository;
import com.example.cosmo_cats_marketplace.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@DisplayName("Product Service Tests")
class ProductServiceImplTest {

    private ProductService productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    @DisplayName("Should create a new product successfully")
    void shouldCreateProduct() {
        Category category = Category.builder().id(1L).name("Test Category").build();
        Product product = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .price(100.0)
                .manufacturer("Test Manufacturer")
                .category(category)
                .build();

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("Test Category");

        ProductEntity savedEntity = ProductEntity.builder()
                .id(1L)
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .manufacturer(product.getManufacturer())
                .category(categoryEntity)
                .build();

        when(productRepository.existsByNameAndCategory(product.getName(), categoryEntity)).thenReturn(false);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(savedEntity);

        Long createdProductId = productService.createProduct(product);

        assertNotNull(createdProductId, "Product ID should not be null.");
        assertEquals(1L, createdProductId, "Product ID does not match.");
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should throw ProductAlreadyExistsException when product already exists")
    void shouldThrowProductAlreadyExistsException() {
        Category category = Category.builder().id(1L).name("Test Category").build();
        Product product = Product.builder()
                .name("Duplicate Product")
                .description("Duplicate Description")
                .price(120.0)
                .manufacturer("Duplicate Manufacturer")
                .category(category)
                .build();

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("Test Category");

        when(productRepository.existsByNameAndCategory(product.getName(), categoryEntity)).thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(product));
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should retrieve all products successfully")
    void shouldGetAllProducts() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("Test Category");

        ProductEntity product1 = ProductEntity.builder()
                .id(1L)
                .name("Product 1")
                .description("Description 1")
                .price(50.0)
                .manufacturer("Manufacturer 1")
                .category(categoryEntity)
                .build();

        ProductEntity product2 = ProductEntity.builder()
                .id(2L)
                .name("Product 2")
                .description("Description 2")
                .price(70.0)
                .manufacturer("Manufacturer 2")
                .category(categoryEntity)
                .build();

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products, "Product list should not be null.");
        assertEquals(2, products.size(), "Product list size does not match.");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve a product by ID successfully")
    void shouldGetProductById() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("Test Category");

        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(100.0)
                .manufacturer("Test Manufacturer")
                .category(categoryEntity)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        Product product = productService.getProductById(1L);

        assertNotNull(product, "Product should not be null.");
        assertEquals("Test Product", product.getName(), "Product name does not match.");
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product is not found by ID")
    void shouldThrowProductNotFoundException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProduct() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("Test Category");

        ProductEntity existingProduct = ProductEntity.builder()
                .id(1L)
                .name("Old Name")
                .description("Old Description")
                .price(80.0)
                .manufacturer("Old Manufacturer")
                .category(categoryEntity)
                .build();

        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Updated Name")
                .description("Updated Description")
                .price(100.0)
                .manufacturer("Updated Manufacturer")
                .category(Category.builder().id(1L).name("Test Category").build())
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(existingProduct);

        productService.updateProduct(updatedProduct);

        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when updating non-existent product")
    void shouldThrowProductNotFoundExceptionOnUpdate() {
        Product product = Product.builder()
                .id(999L)
                .name("Non-existent Product")
                .build();

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(product));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProductById(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}
