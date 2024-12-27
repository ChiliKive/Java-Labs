package com.example.cosmo_cats_marketplace.service.impl;

import com.example.cosmo_cats_marketplace.domain.Customer;
import com.example.cosmo_cats_marketplace.entity.CustomerEntity;
import com.example.cosmo_cats_marketplace.exception.service.CustomerNotFoundException;
import com.example.cosmo_cats_marketplace.mapper.CustomerMapper;
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
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            log.warn("Customer with email {} already exists.", customer.getEmail());
            throw new IllegalArgumentException("Customer with this email already exists.");
        }
        CustomerEntity entity = customerMapper.toEntity(customer);
        CustomerEntity savedEntity = customerRepository.save(entity);
        log.info("Customer created with ID {}", savedEntity.getId());
        return customerMapper.toDomain(savedEntity);
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<CustomerEntity> entities = customerRepository.findAll();
        if (entities.isEmpty()) {
            log.warn("No customers found.");
        }
        return entities.stream()
                .map(customerMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Customer getCustomerById(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer with ID {} not found.", id);
                    return new CustomerNotFoundException(id);
                });
        return customerMapper.toDomain(entity);
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
        log.info("Customer with ID {} updated.", id);
        return customerMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            log.info("Customer with ID {} deleted.", id);
        } else {
            log.warn("Customer with ID {} not found for deletion.", id);
        };
    }
}
