package com.camtech.bi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "erp-service", url = "${erp.service.url}")
public interface ErpClient {
    @GetMapping("/api/erp/employees")
    List<Map<String, Object>> getEmployees();
}
