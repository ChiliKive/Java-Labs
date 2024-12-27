package com.example.cosmo_cats_marketplace.service.impl;

import com.example.cosmo_cats_marketplace.domain.Customer;
import com.example.cosmo_cats_marketplace.service.CustomerService;
import com.example.cosmo_cats_marketplace.exception.service.CustomerNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final List<Customer> customers;

    public CustomerServiceImpl() {
        this.customers = new ArrayList<>(buildAllCustomersMock());
    }

    @Override
    public Customer getCustomerById(UUID id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Customer with ID {} not found.", id);
                    return new CustomerNotFoundException(id);
                });
    }

    @Override
    public List<Customer> getAllCustomers() {
        if (customers.isEmpty()) {
            log.warn("No customers found.");
        }
        return new ArrayList<>(customers);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer newCustomer = buildCustomer(customer.toBuilder()
                .id(UUID.randomUUID())
                .build());
        customers.add(newCustomer);
        return newCustomer;
    }

    @Override
    public Customer updateCustomer(UUID id, Customer customer) {
        Customer existingCustomer = getCustomerById(id);
        Customer updatedCustomer = buildCustomer(customer.toBuilder()
                .id(existingCustomer.getId())
                .build());
        customers.set(customers.indexOf(existingCustomer), updatedCustomer);
        return updatedCustomer;
    }

    @Override
    public void deleteCustomer(UUID id) {
        Customer customer = getCustomerById(id);
        customers.remove(customer);
    }

    private Customer buildCustomer(Customer customer) {
        return customer.toBuilder()
                .name(customer.getName())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .build();
    }

    private List<Customer> buildAllCustomersMock() {
        return List.of(
                Customer.builder()
                        .id(UUID.fromString("fb9eb420-0db2-4905-af78-2802486ea617"))
                        .name("Alice Johnson")
                        .address("789 Nebula Way")
                        .phone("+12345678901")
                        .email("alice.johnson@example.com")
                        .build(),
                Customer.builder()
                        .id(UUID.fromString("7b60ddfd-2744-4a84-913e-9e0427498bb0"))
                        .name("Bob Brown")
                        .address("321 Meteor Lane")
                        .phone("+10987654321")
                        .email("bob.brown@example.com")
                        .build()
        );
    }
}
