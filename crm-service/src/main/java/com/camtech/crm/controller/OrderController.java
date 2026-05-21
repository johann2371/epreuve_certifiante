package com.camtech.crm.controller;

import com.camtech.crm.entity.Customer;
import com.camtech.crm.entity.Order;
import com.camtech.crm.repository.CustomerRepository;
import com.camtech.crm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/crm/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        if (order.getCustomer() == null || order.getCustomer().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Customer> customerOpt = customerRepository.findById(order.getCustomer().getId());
        if (customerOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        order.setCustomer(customerOpt.get());
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }
}
