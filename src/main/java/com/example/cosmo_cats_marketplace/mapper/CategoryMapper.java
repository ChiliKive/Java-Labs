package com.example.cosmo_cats_marketplace.mapper;

import com.example.cosmo_cats_marketplace.domain.Category;
import com.example.cosmo_cats_marketplace.dto.category.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  CategoryDto toCategoryDto(Category category);
}
