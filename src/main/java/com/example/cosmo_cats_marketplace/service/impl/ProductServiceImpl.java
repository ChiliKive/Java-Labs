package com.example.cosmo_cats_marketplace.service.impl;

import com.example.cosmo_cats_marketplace.domain.Product;
import com.example.cosmo_cats_marketplace.domain.Category;
import com.example.cosmo_cats_marketplace.entity.ProductEntity;
import com.example.cosmo_cats_marketplace.entity.CategoryEntity;
import com.example.cosmo_cats_marketplace.exception.service.ProductNotFoundException;
import com.example.cosmo_cats_marketplace.exception.service.ProductAlreadyExistsException;
import com.example.cosmo_cats_marketplace.repository.ProductRepository;
import com.example.cosmo_cats_marketplace.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Long createProduct(Product product) {
        CategoryEntity categoryEntity = mapCategoryToEntity(product.getCategory());
        if (productRepository.existsByNameAndCategory(product.getName(), categoryEntity)) {
            throw new ProductAlreadyExistsException(product.getName());
        }
        ProductEntity entity = mapToEntity(product);
        ProductEntity savedEntity = productRepository.save(entity);
        return savedEntity.getId();
    }

    @Override
    public List<Product> getAllProducts() {
        List<ProductEntity> entities = productRepository.findAll();
        if (entities.isEmpty()) {
            log.warn("No products found.");
        }
        return entities.stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Product getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product with ID {} not found.", id);
                    return new ProductNotFoundException(id);
                });
        return mapToDomain(entity);
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {
        ProductEntity existingEntity = productRepository.findById(product.getId())
                .orElseThrow(() -> {
                    log.warn("Product with ID {} not found.", product.getId());
                    return new ProductNotFoundException(product.getId());
                });

        CategoryEntity categoryEntity = mapCategoryToEntity(product.getCategory());
        if (productRepository.existsByNameAndCategoryAndIdNot(product.getName(), categoryEntity, product.getId())) {
            throw new ProductAlreadyExistsException(product.getName());
        }

        existingEntity.setName(product.getName());
        existingEntity.setDescription(product.getDescription());
        existingEntity.setPrice(product.getPrice());
        existingEntity.setManufacturer(product.getManufacturer());
        existingEntity.setCategory(categoryEntity);

        productRepository.save(existingEntity);
    }

    @Override
    @Transactional
    public void deleteProductById(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.deleteById(productId);
    }

    private Product mapToDomain(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .manufacturer(entity.getManufacturer())
                .category(mapCategoryToDomain(entity.getCategory()))
                .build();
    }

    private ProductEntity mapToEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setManufacturer(product.getManufacturer());
        entity.setCategory(mapCategoryToEntity(product.getCategory()));
        return entity;
    }

    private Category mapCategoryToDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    private CategoryEntity mapCategoryToEntity(Category category) {
        if (category == null) return null;
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        return entity;
    }
}
