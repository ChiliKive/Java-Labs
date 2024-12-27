package com.example.cosmo_cats_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.cosmo_cats_marketplace.domain.Customer;
import com.example.cosmo_cats_marketplace.exception.service.CustomerNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@DisplayName("Enhanced Customer Service Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    private static UUID testCustomerId;
    private static final String UPDATED_CUSTOMER_NAME = "Updated Customer Name";

    @Test
    @Order(1)
    void shouldReturnAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        assertNotNull(customers, "The customer list should not be null.");
        assertEquals(2, customers.size(), "The size of the customer list does not match the expected value.");
    }

    @Test
    @Order(2)
    void shouldCreateCustomer() {
        Customer newCustomer = Customer.builder()
                .name("New Customer")
                .address("456 Comet Trail")
                .phone("+9876543210")
                .email("new.customer@example.com")
                .build();

        Customer createdCustomer = customerService.createCustomer(newCustomer);
        testCustomerId = createdCustomer.getId();

        assertNotNull(createdCustomer, "Created customer should not be null.");
        assertEquals(newCustomer.getName(), createdCustomer.getName(), "Customer names do not match.");
        assertEquals(newCustomer.getAddress(), createdCustomer.getAddress(), "Customer addresses do not match.");
        assertEquals(newCustomer.getPhone(), createdCustomer.getPhone(), "Customer phone numbers do not match.");
        assertEquals(newCustomer.getEmail(), createdCustomer.getEmail(), "Customer emails do not match.");
    }

    @Test
    @Order(3)
    void shouldUpdateCustomer() {
        Customer updatedCustomer = Customer.builder()
                .id(testCustomerId)
                .name(UPDATED_CUSTOMER_NAME)
                .address("456 Comet Trail")
                .phone("+9876543210")
                .email("updated.customer@example.com")
                .build();

        Customer result = customerService.updateCustomer(testCustomerId, updatedCustomer);
        assertNotNull(result, "Updated customer should not be null.");
        assertEquals(UPDATED_CUSTOMER_NAME, result.getName(), "Updated customer name does not match.");
        assertEquals(updatedCustomer.getAddress(), result.getAddress(), "Updated customer address does not match.");
        assertEquals(updatedCustomer.getPhone(), result.getPhone(), "Updated customer phone does not match.");
        assertEquals(updatedCustomer.getEmail(), result.getEmail(), "Updated customer email does not match.");
    }

    @Test
    @Order(4)
    void shouldThrowCustomerNotFoundExceptionOnUpdate() {
        UUID invalidId = UUID.randomUUID();

        Customer nonExistentCustomer = Customer.builder()
                .id(invalidId)
                .name("Non Existent Customer")
                .address("Unknown Address")
                .phone("+1111111111")
                .email("non.existent@example.com")
                .build();

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(invalidId, nonExistentCustomer));
    }

    @Test
    @Order(5)
    void shouldDeleteCustomer() {
        customerService.deleteCustomer(testCustomerId);
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(testCustomerId));
    }

    @Test
    @Order(6)
    void shouldThrowCustomerNotFoundExceptionForInvalidId() {
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(UUID.randomUUID()));
    }

    @Test
    @Order(7)
    void shouldThrowExceptionWhenNoCustomers() {
        List<Customer> allCustomers = new ArrayList<>(customerService.getAllCustomers());
        for (Customer customer : allCustomers) {
            customerService.deleteCustomer(customer.getId());
        }

        List<Customer> remainingCustomers = customerService.getAllCustomers();
        assertTrue(remainingCustomers.isEmpty(), "Customer list should be empty.");
    }
}
