package com.example.cosmo_cats_marketplace.config;

import com.example.cosmo_cats_marketplace.mapper.CustomerMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.cosmo_cats_marketplace.mapper.CategoryMapper;
import com.example.cosmo_cats_marketplace.mapper.ProductMapper;

@TestConfiguration
public class MappersTestConfiguration {

    @Bean
    public ProductMapper productMapper() {
        return Mappers.getMapper(ProductMapper.class);
    }

    @Bean
    public CategoryMapper categoryMapper() {
        return Mappers.getMapper(CategoryMapper.class);
    }

    @Bean
    public CustomerMapper customerMapper() {
        return Mappers.getMapper(CustomerMapper.class);
    }
}

