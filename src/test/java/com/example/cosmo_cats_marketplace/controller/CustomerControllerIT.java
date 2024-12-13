package com.example.cosmo_cats_marketplace.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cosmo_cats_marketplace.config.MappersTestConfiguration;
import com.example.cosmo_cats_marketplace.domain.Customer;
import com.example.cosmo_cats_marketplace.dto.Customer.CustomerDto;
import com.example.cosmo_cats_marketplace.dto.Customer.CustomerListDto;
import com.example.cosmo_cats_marketplace.exception.GlobalExceptionHandler;
import com.example.cosmo_cats_marketplace.exception.service.CustomerNotFoundException;
import com.example.cosmo_cats_marketplace.mapper.CustomerMapper;
import com.example.cosmo_cats_marketplace.service.CustomerService;
import com.example.cosmo_cats_marketplace.service.impl.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(CustomerController.class)
@Import({MappersTestConfiguration.class, GlobalExceptionHandler.class})
@DisplayName("Customer Controller IT")
public class CustomerControllerIT {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerMapper customerMapper;

    @Test
    void shouldReturnAllCustomers() throws Exception {
        List<Customer> customers = new CustomerServiceImpl().getAllCustomers();
        CustomerListDto customerListDto = customerMapper.toCustomerListDto(customers);

        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers[0].name").value(customers.get(0).getName()))
                .andExpect(jsonPath("$.customers[1].name").value(customers.get(1).getName()));
    }

    @Test
    void shouldReturnCustomerById() throws Exception {
        Customer customer = new CustomerServiceImpl().getAllCustomers().get(0);

        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customers/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.address").value(customer.getAddress()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()));
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        CustomerServiceImpl customerServiceMock = new CustomerServiceImpl();
        Customer newCustomer = customerServiceMock.getAllCustomers().get(0);
        CustomerDto customerDto = customerMapper.toCustomerDto(newCustomer);

        when(customerService.createCustomer(any(Customer.class))).thenReturn(newCustomer);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newCustomer.getName()))
                .andExpect(jsonPath("$.address").value(newCustomer.getAddress()))
                .andExpect(jsonPath("$.phone").value(newCustomer.getPhone()))
                .andExpect(jsonPath("$.email").value(newCustomer.getEmail()));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        CustomerServiceImpl customerServiceMock = new CustomerServiceImpl();
        Customer existingCustomer = customerServiceMock.getAllCustomers().get(0);
        Customer updatedCustomer = existingCustomer.toBuilder()
                .name("Updated Name")
                .build();
        CustomerDto updatedCustomerDto = customerMapper.toCustomerDto(updatedCustomer);

        when(customerService.updateCustomer(eq(existingCustomer.getId()), any(Customer.class)))
                .thenReturn(updatedCustomer);

        mockMvc.perform(put("/api/v1/customers/{id}", existingCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedCustomerDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedCustomer.getName()))
                .andExpect(jsonPath("$.address").value(updatedCustomer.getAddress()))
                .andExpect(jsonPath("$.phone").value(updatedCustomer.getPhone()))
                .andExpect(jsonPath("$.email").value(updatedCustomer.getEmail()));
    }


    @Test
    void shouldDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundForInvalidCustomerId() throws Exception {
        doThrow(new CustomerNotFoundException(999L)).when(customerService).getCustomerById(999L);

        mockMvc.perform(get("/api/v1/customers/{id}", 999L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("customer-not-found"));
    }
}
