package com.example.cosmo_cats_marketplace.controller;

import com.example.cosmo_cats_marketplace.domain.Customer;
import com.example.cosmo_cats_marketplace.dto.Customer.CustomerDto;
import com.example.cosmo_cats_marketplace.dto.Customer.CustomerListDto;
import com.example.cosmo_cats_marketplace.mapper.CustomerMapper;
import com.example.cosmo_cats_marketplace.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @GetMapping
    public ResponseEntity<CustomerListDto> getAllCustomers() {
        return ResponseEntity.ok(customerMapper.toCustomerListDto(customerService.getAllCustomers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerMapper.toCustomerDto(customerService.getCustomerById(id)));
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CustomerDto customerDto) {
        Customer newCustomer = customerService.createCustomer(customerMapper.toCustomer(customerDto));
        return ResponseEntity.ok(customerMapper.toCustomerDto(newCustomer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable Long id,
            @RequestBody @Valid CustomerDto customerDto) {
        Customer updatedCustomer = customerService.updateCustomer(id, customerMapper.toCustomer(customerDto));
        return ResponseEntity.ok(customerMapper.toCustomerDto(updatedCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
