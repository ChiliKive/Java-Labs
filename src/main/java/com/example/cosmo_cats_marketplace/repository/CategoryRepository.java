package com.example.cosmo_cats_marketplace.repository;

import com.example.cosmo_cats_marketplace.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
