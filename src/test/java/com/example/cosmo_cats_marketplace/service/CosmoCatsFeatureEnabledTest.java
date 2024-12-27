package com.example.cosmo_cats_marketplace.service;

import com.example.cosmo_cats_marketplace.service.impl.CosmoCatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "feature.cosmoCats.enabled=true")
public class CosmoCatsFeatureEnabledTest {

    @Autowired
    private CosmoCatsService cosmoCatsService;

    @Test
    void testFeatureEnabled() {
        assertDoesNotThrow(() -> cosmoCatsService.getCosmoCats());
    }
}
