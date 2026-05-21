package com.camtech.bi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "supply-service", url = "${supply.service.url}")
public interface SupplyClient {
    @GetMapping("/api/supply/shipments")
    List<Map<String, Object>> getShipments();
}
