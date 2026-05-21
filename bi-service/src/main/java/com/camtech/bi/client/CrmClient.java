package com.camtech.bi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "crm-service", url = "${crm.service.url}")
public interface CrmClient {
    @GetMapping("/api/crm/customers")
    List<Map<String, Object>> getCustomers();
    
    @GetMapping("/api/crm/orders")
    List<Map<String, Object>> getOrders();
}
