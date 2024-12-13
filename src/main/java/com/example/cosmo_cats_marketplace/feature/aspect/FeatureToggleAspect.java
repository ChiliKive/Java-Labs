package com.example.cosmo_cats_marketplace.feature.aspect;


import com.example.cosmo_cats_marketplace.feature.annotation.RequiresFeatureToggle;
import com.example.cosmo_cats_marketplace.feature.exception.FeatureNotAvailableException;
import com.example.cosmo_cats_marketplace.service.impl.FeatureToggleService;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    @Before("@annotation(toggle)")
    public void checkFeatureToggle(RequiresFeatureToggle toggle) {
        String feature = toggle.feature();

        boolean isEnabled = switch (feature) {
            case "cosmoCats" -> featureToggleService.isCosmoCatsEnabled();
            case "kittyProducts" -> featureToggleService.isKittyProductsEnabled();
            default -> throw new IllegalArgumentException("Unknown feature: " + feature);
        };

        if (!isEnabled) {
            throw new FeatureNotAvailableException("Feature " + feature + " is not enabled.");
        }
    }
}
