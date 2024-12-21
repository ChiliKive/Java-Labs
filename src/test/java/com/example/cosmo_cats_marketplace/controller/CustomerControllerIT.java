package com.example.cosmo_cats_marketplace.controller;

import com.example.cosmo_cats_marketplace.AbstractTestcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.cosmo_cats_marketplace.dto.Customer.CustomerDto;
import com.example.cosmo_cats_marketplace.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIT extends AbstractTestcontainers {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Test
    void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk());
    }


    @Test
    void testCreateCustomer() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .name("John Doe")
                .address("123 Street")
                .phone("+1234567890")
                .email("john.doe@example.com")
                .build();

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customer))) // Сериализация объекта в JSON
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()));
    }

}
