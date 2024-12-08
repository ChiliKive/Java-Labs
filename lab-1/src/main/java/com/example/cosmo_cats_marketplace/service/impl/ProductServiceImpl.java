package com.example.cosmo_cats_marketplace.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.cosmo_cats_marketplace.domain.Product;
import com.example.cosmo_cats_marketplace.domain.Category;
import com.example.cosmo_cats_marketplace.domain.Customer;
import com.example.cosmo_cats_marketplace.service.CustomerService;
import com.example.cosmo_cats_marketplace.service.ProductService;
import com.example.cosmo_cats_marketplace.exception.service.ProductAlreadyExistsException;
import com.example.cosmo_cats_marketplace.exception.service.ProductNotFoundException;
import com.example.cosmo_cats_marketplace.exception.service.ProductsNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final List<Product> products;

    public ProductServiceImpl(CustomerService customerService){
        this.products = buildAllProductsMock();
    }

    @Override
    public List<Product> getAllProducts() {
        if (products.isEmpty()){
            throw new ProductsNotFoundException();
        }
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        return products.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> {
                log.warn("A product with {} id not found in mock.", id);
                return new ProductNotFoundException(id);
            });
    }

    @Override
    public Long createProduct(Product product) {
        if (products.stream().anyMatch(p -> p.getName().equalsIgnoreCase(product.getName()))) {
            throw new ProductAlreadyExistsException(product.getName());
        }
        Product newProduct = buildProduct(product.toBuilder().id((long) (products.size() + 1)).build());
        products.add(newProduct);
        return newProduct.getId();
    }

    @Override
    public void updateProduct(Product product) {
        Product existingProduct = getProductById(product.getId());
        if (products.stream().anyMatch(p -> !p.getId().equals(product.getId()) && p.getName().equalsIgnoreCase(product.getName()))) {
            throw new ProductAlreadyExistsException(product.getName());
        }
        Product updatedProduct = buildProduct(product.toBuilder().id(existingProduct.getId()).build());
        products.set(products.indexOf(existingProduct), updatedProduct);
    }

    @Override
    public void deleteProductById(Long productId) {
        Product product = getProductById(productId);
        products.remove(product);
    }

    private Product buildProduct(Product product) {
        return product.toBuilder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .manufacturer(product.getManufacturer())
                .category(product.getCategory())
                .build();
    }

    private List<Product> buildAllProductsMock() {
        List<Product> products = new ArrayList<>();

        Category spaceFood = Category.builder()
                .id(1L)
                .name("Space Food Supplies")
                .build();
        Category cosmicGadgets = Category.builder()
                .id(2L)
                .name("Cosmic Gadgets")
                .build();

        products.add(Product.builder()
                .id(1L)
                .name("Astronaut Tuna Cans")
                .description("Premium tuna packed for interstellar expeditions.")
                .price(20.0)
                .manufacturer("SpaceFood Industries")
                .category(spaceFood)
                .build());

        products.add(Product.builder()
                .id(2L)
                .name("Stellar Laser Pointer")
                .description("A laser pointer designed for entertaining cosmic pets.")
                .price(35.0)
                .manufacturer("Gadget Galaxy")
                .category(cosmicGadgets)
                .build());

        products.add(Product.builder()
                .id(3L)
                .name("Zero-Gravity Pet Suit")
                .description("Keep your pets safe and stylish in zero gravity.")
                .price(150.0)
                .manufacturer("CosmoPet Co.")
                .category(cosmicGadgets)
                .build());

        return products;
    }
}
