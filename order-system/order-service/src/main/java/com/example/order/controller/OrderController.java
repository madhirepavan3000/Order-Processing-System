package com.example.order.controller;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.model.Order;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping
    public Order create(@RequestBody CreateOrderRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<Order> list() {
        return service.findAll();
    }
}
