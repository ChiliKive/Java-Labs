package com.example.cosmo_cats_marketplace.repository;

import com.example.cosmo_cats_marketplace.AbstractTestcontainers;
import com.example.cosmo_cats_marketplace.entity.CategoryEntity;
import com.example.cosmo_cats_marketplace.entity.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Product Repository IT")
public class ProductRepositoryIT extends AbstractTestcontainers {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should create and find a product")
    void shouldCreateAndFindProduct() {
        CategoryEntity category = new CategoryEntity();
        category.setName("Test Category");
        categoryRepository.save(category);

        ProductEntity product = new ProductEntity();
        product.setName("Test Product");
        product.setDescription("A product for testing");
        product.setPrice(99.99);
        product.setManufacturer("Test Manufacturer");
        product.setCategory(category);
        productRepository.save(product);

        Optional<ProductEntity> foundProduct = productRepository.findById(product.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("Should update a product")
    void shouldUpdateProduct() {
        CategoryEntity category = new CategoryEntity();
        category.setName("Update Category");
        categoryRepository.save(category);

        ProductEntity product = new ProductEntity();
        product.setName("Update Test");
        product.setDescription("Before Update");
        product.setPrice(50.0);
        product.setManufacturer("Update Manufacturer");
        product.setCategory(category);
        productRepository.save(product);

        product.setDescription("After Update");
        productRepository.save(product);

        ProductEntity updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getDescription()).isEqualTo("After Update");
    }

    @Test
    @DisplayName("Should delete a product")
    void shouldDeleteProduct() {
        CategoryEntity category = new CategoryEntity();
        category.setName("Delete Category");
        categoryRepository.save(category);

        ProductEntity product = new ProductEntity();
        product.setName("Delete Test");
        product.setDescription("To Be Deleted");
        product.setPrice(10.0);
        product.setManufacturer("Delete Manufacturer");
        product.setCategory(category);
        productRepository.save(product);

        productRepository.deleteById(product.getId());

        Optional<ProductEntity> deletedProduct = productRepository.findById(product.getId());
        assertThat(deletedProduct).isEmpty();
    }
}
