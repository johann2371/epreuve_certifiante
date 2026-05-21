package com.camtech.supply.controller;

import com.camtech.supply.entity.Shipment;
import com.camtech.supply.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShipmentController.class)
public class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentRepository shipmentRepository;

    @Test
    public void testGetAllShipments() throws Exception {
        Shipment shipment = new Shipment(1L, "Bananes", 500, "Plantation A", "Usine B", "IN_TRANSIT", LocalDateTime.now());
        Mockito.when(shipmentRepository.findAll()).thenReturn(Arrays.asList(shipment));

        mockMvc.perform(get("/api/supply/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productType").value("Bananes"))
                .andExpect(jsonPath("$[0].status").value("IN_TRANSIT"));
    }
}
