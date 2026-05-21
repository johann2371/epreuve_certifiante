package com.camtech.crm.controller;

import com.camtech.crm.entity.Customer;
import com.camtech.crm.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void testGetAllCustomers() throws Exception {
        Customer customer = new Customer(1L, "Alice", "alice@test.com", "123456789", 100);
        Mockito.when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        mockMvc.perform(get("/api/crm/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].loyaltyPoints").value(100));
    }
}
