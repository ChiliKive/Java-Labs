package com.example.cosmo_cats_marketplace.exception.service;

public class CustomerNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Customer with ID '%d' was not found in the system";

    public CustomerNotFoundException(Long id){
        super(String.format(MESSAGE_TEMPLATE, id));
    }
    
}
