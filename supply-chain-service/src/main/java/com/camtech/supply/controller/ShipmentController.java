package com.camtech.supply.controller;

import com.camtech.supply.entity.Shipment;
import com.camtech.supply.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/supply/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @PostMapping
    public Shipment createShipment(@RequestBody Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Shipment> updateShipmentStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<Shipment> shipmentOpt = shipmentRepository.findById(id);
        if (shipmentOpt.isPresent()) {
            Shipment shipment = shipmentOpt.get();
            shipment.setStatus(status);
            return ResponseEntity.ok(shipmentRepository.save(shipment));
        }
        return ResponseEntity.notFound().build();
    }
}
