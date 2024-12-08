package com.example.cosmo_cats_marketplace.exception.controller;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
@AllArgsConstructor
public class ValidationErrorDetails {
    String parameterName;
    String errorMessage;
}
