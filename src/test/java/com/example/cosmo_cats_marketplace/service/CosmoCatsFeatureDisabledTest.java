package com.example.cosmo_cats_marketplace.service;

import com.example.cosmo_cats_marketplace.AbstractTestcontainers;
import com.example.cosmo_cats_marketplace.feature.exception.FeatureNotAvailableException;
import com.example.cosmo_cats_marketplace.service.impl.CosmoCatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = "feature.cosmoCats.enabled=false")
@DisplayName("CosmoCats Feature Disabled Test")
public class CosmoCatsFeatureDisabledTest extends AbstractTestcontainers {

    @Autowired
    private CosmoCatsService cosmoCatsService;

    @Test
    void testFeatureDisabled() {
        FeatureNotAvailableException exception = assertThrows(
                FeatureNotAvailableException.class,
                () -> cosmoCatsService.getCosmoCats()
        );
        assertEquals("Feature cosmoCats is not enabled.", exception.getMessage());
    }
}
