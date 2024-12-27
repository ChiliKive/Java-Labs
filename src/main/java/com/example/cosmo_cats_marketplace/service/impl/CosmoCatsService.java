package com.example.cosmo_cats_marketplace.service.impl;

import com.example.cosmo_cats_marketplace.feature.annotation.RequiresFeatureToggle;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CosmoCatsService {
    private final FeatureToggleService featureToggleService;

    @RequiresFeatureToggle(feature = "cosmoCats")
    public String getCosmoCats() {
        return "Cosmo Cats are ready for intergalactic adventures!";
    }
}
