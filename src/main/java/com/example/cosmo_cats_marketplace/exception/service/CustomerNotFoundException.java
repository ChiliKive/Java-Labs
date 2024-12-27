package com.example.cosmo_cats_marketplace.exception.service;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Customer with ID '%s' was not found in the system";

    public CustomerNotFoundException(UUID id){
        super(String.format(MESSAGE_TEMPLATE, id));
    }
    
}
