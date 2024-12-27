package com.example.cosmo_cats_marketplace.controller;

import com.example.cosmo_cats_marketplace.AbstractIt;
import com.example.cosmo_cats_marketplace.dto.customer.CustomerDto;
import com.example.cosmo_cats_marketplace.mapper.CustomerMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DisplayName("Customer Controller IT")
@Tag("customer-service")
class CustomerControllerIT extends AbstractIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerMapper customerMapper;

    private final CustomerDto TEST_CUSTOMER = CustomerDto.builder()
            .id(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"))
            .name("Test Customer")
            .address("Sector 5, Planet Zeta, Quadrant 12")
            .phone("+1234567890")
            .email("test@example.com")
            .build();

    @Test
    @DisplayName("Should fetch all customers successfully")
    void shouldGetAllCustomers() throws Exception {
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers").isArray())
                .andExpect(jsonPath("$.customers.length()").value(2));
    }

    @Test
    @DisplayName("Should fetch a single customer by ID successfully")
    void shouldGetCustomerById() throws Exception {
        CustomerDto customerDto = createTestCustomer();

        mockMvc.perform(get("/api/v1/customers/{id}", customerDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TEST_CUSTOMER.getName()))
                .andExpect(jsonPath("$.address").value(TEST_CUSTOMER.getAddress()))
                .andExpect(jsonPath("$.phone").value(TEST_CUSTOMER.getPhone()))
                .andExpect(jsonPath("$.email").value(TEST_CUSTOMER.getEmail()));
    }

    @Test
    @DisplayName("Should create a new customer successfully")
    void shouldCreateCustomer() throws Exception {
        String customerJson = objectMapper.writeValueAsString(TEST_CUSTOMER);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TEST_CUSTOMER.getName()))
                .andExpect(jsonPath("$.address").value(TEST_CUSTOMER.getAddress()));
    }

    @Test
    @DisplayName("Should handle validation error when creating a customer")
    void shouldFailToCreateCustomerWithInvalidData() throws Exception {
        String invalidCustomerJson = """
        {
            "name": "T",
            "address": "",
            "phone": "123",
            "email": "invalid-email"
        }
        """;

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCustomerJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.invalidParams").isArray());
    }

    private CustomerDto createTestCustomer() throws Exception {
        CustomerDto customerDto = CustomerDto.builder()
                .name("Test Customer")
                .address("Sector 5, Planet Zeta, Quadrant 12")
                .phone("+1234567890")
                .email("test@example.com")
                .build();

        String customerJson = objectMapper.writeValueAsString(customerDto);

        String responseJson = mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UUID customerId = UUID.fromString(objectMapper.readTree(responseJson).get("id").asText());
        return customerDto.toBuilder().id(customerId).build();
    }
}
