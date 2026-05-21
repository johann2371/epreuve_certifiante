package com.camtech.crm.controller;

import com.camtech.crm.entity.Customer;
import com.camtech.crm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/crm/loyalty")
public class LoyaltyController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Integer> getLoyaltyPoints(@PathVariable Long id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isPresent()) {
            return ResponseEntity.ok(customerOpt.get().getLoyaltyPoints());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateLoyaltyPoints(@PathVariable Long id, @RequestParam Integer points) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
            return ResponseEntity.ok(customerRepository.save(customer));
        }
        return ResponseEntity.notFound().build();
    }
}
