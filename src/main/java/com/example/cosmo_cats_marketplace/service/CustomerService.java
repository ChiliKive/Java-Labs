package com.example.cosmo_cats_marketplace.service;

import com.example.cosmo_cats_marketplace.domain.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    Customer getCustomerById(UUID id);
    List<Customer> getAllCustomers();
    Customer createCustomer(Customer customer);
    Customer updateCustomer(UUID id, Customer customer);
    void deleteCustomer(UUID id);
}
