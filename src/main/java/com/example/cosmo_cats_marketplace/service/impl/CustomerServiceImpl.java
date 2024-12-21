package com.example.cosmo_cats_marketplace.service.impl;

import com.example.cosmo_cats_marketplace.domain.Customer;
import com.example.cosmo_cats_marketplace.entity.CustomerEntity;
import com.example.cosmo_cats_marketplace.exception.service.CustomerNotFoundException;
import com.example.cosmo_cats_marketplace.repository.CustomerRepository;
import com.example.cosmo_cats_marketplace.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName(customer.getName());
        customerEntity.setAddress(customer.getAddress());
        customerEntity.setPhone(customer.getPhone());
        customerEntity.setEmail(customer.getEmail());
        CustomerEntity savedEntity = customerRepository.save(customerEntity);
        return mapToDomain(savedEntity);
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<CustomerEntity> entities = customerRepository.findAll();
        if (entities.isEmpty()) {
            log.warn("No customers found.");
        }
        return entities.stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Customer getCustomerById(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer with ID {} not found.", id);
                    return new CustomerNotFoundException(id);
                });
        return mapToDomain(entity);
    }

    @Override
    @Transactional
    public Customer updateCustomer(Long id, Customer customer) {
        CustomerEntity existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer with ID {} not found.", id);
                    return new CustomerNotFoundException(id);
                });
        existingCustomer.setName(customer.getName());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setEmail(customer.getEmail());
        CustomerEntity updatedEntity = customerRepository.save(existingCustomer);
        return mapToDomain(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }

    private Customer mapToDomain(CustomerEntity entity) {
        return Customer.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .build();
    }
}
