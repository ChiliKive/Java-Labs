package com.example.cosmo_cats_marketplace.repository;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


public abstract class AbstractTestcontainers {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    @BeforeAll
    static void setup() {
        System.setProperty("spring.datasource.url", POSTGRES_CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username", POSTGRES_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", POSTGRES_CONTAINER.getPassword());

        System.setProperty("spring.liquibase.url", POSTGRES_CONTAINER.getJdbcUrl());
        System.setProperty("spring.liquibase.user", POSTGRES_CONTAINER.getUsername());
        System.setProperty("spring.liquibase.password", POSTGRES_CONTAINER.getPassword());
    }
}
