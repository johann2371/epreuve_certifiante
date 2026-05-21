package com.camtech.erp.controller;

import com.camtech.erp.entity.Employee;
import com.camtech.erp.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    public void testGetAllEmployees() throws Exception {
        Employee emp = new Employee(1L, "Jean", "Dupont", "jean@test.com", "Dev", 2000.0, LocalDate.now());
        Mockito.when(employeeRepository.findAll()).thenReturn(Arrays.asList(emp));

        mockMvc.perform(get("/api/erp/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jean"))
                .andExpect(jsonPath("$[0].email").value("jean@test.com"));
    }
}
