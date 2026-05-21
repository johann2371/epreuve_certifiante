package com.camtech.bi.controller;

import com.camtech.bi.client.CrmClient;
import com.camtech.bi.client.ErpClient;
import com.camtech.bi.client.SupplyClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErpClient erpClient;

    @MockBean
    private CrmClient crmClient;

    @MockBean
    private SupplyClient supplyClient;

    @Test
    public void testGetOverview() throws Exception {
        Mockito.when(erpClient.getEmployees()).thenReturn(new ArrayList<>());
        Mockito.when(crmClient.getCustomers()).thenReturn(new ArrayList<>());
        Mockito.when(crmClient.getOrders()).thenReturn(new ArrayList<>());
        Mockito.when(supplyClient.getShipments()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/bi/dashboard/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employees_count").value(0))
                .andExpect(jsonPath("$.customers_count").value(0));
    }
}
