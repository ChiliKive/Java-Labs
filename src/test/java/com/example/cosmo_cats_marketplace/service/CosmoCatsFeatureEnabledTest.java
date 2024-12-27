package com.example.cosmo_cats_marketplace.service;

import com.example.cosmo_cats_marketplace.AbstractTestcontainers;
import com.example.cosmo_cats_marketplace.service.impl.CosmoCatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "feature.cosmoCats.enabled=true")
@DisplayName("Cosmo Cats Feature Enabled Test")
public class CosmoCatsFeatureEnabledTest extends AbstractTestcontainers {

    @Autowired
    private CosmoCatsService cosmoCatsService;

    @Test
    void testFeatureEnabled() {
        assertDoesNotThrow(() -> cosmoCatsService.getCosmoCats());
    }
}
