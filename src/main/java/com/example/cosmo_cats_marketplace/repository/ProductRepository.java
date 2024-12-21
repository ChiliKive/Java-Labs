package com.example.cosmo_cats_marketplace.repository;

import com.example.cosmo_cats_marketplace.entity.CategoryEntity;
import com.example.cosmo_cats_marketplace.entity.ProductEntity;
import com.example.cosmo_cats_marketplace.projection.ProductReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query("SELECT p.name AS name, COUNT(o) AS totalSold " +
            "FROM ProductEntity p " +
            "JOIN p.orders o " +
            "WHERE p.category.id = :categoryId " +
            "GROUP BY p.name " +
            "ORDER BY totalSold DESC")
    List<ProductReport> getTopSellingProductsByCategory(@Param("categoryId") Long categoryId);

    boolean existsByNameAndCategory(String name, CategoryEntity category);
    boolean existsByNameAndCategoryAndIdNot(String name, CategoryEntity category, Long id);
}
