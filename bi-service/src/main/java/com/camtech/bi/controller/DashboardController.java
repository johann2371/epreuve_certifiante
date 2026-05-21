package com.camtech.bi.controller;

import com.camtech.bi.client.CrmClient;
import com.camtech.bi.client.ErpClient;
import com.camtech.bi.client.SupplyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bi/dashboard")
public class DashboardController {

    @Autowired
    private ErpClient erpClient;
    
    @Autowired
    private CrmClient crmClient;
    
    @Autowired
    private SupplyClient supplyClient;

    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        Map<String, Object> dashboard = new HashMap<>();
        
        try {
            dashboard.put("employees_count", erpClient.getEmployees().size());
        } catch (Exception e) {
            dashboard.put("employees_count", "ERP Service Unavailable");
        }
        
        try {
            dashboard.put("customers_count", crmClient.getCustomers().size());
            dashboard.put("orders_count", crmClient.getOrders().size());
        } catch (Exception e) {
            dashboard.put("customers_status", "CRM Service Unavailable");
        }
        
        try {
            dashboard.put("shipments_count", supplyClient.getShipments().size());
        } catch (Exception e) {
            dashboard.put("shipments_status", "Supply Chain Service Unavailable");
        }
        
        return dashboard;
    }
}
