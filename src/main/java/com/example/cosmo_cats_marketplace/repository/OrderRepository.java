package com.example.cosmo_cats_marketplace.repository;

import com.example.cosmo_cats_marketplace.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderUuid(UUID uuid);
}
