package com.example.order.service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.OrderEvent;
import com.example.order.kafka.OrderEventProducer;
import com.example.order.model.Order;
import com.example.order.model.OrderStatus;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderEventProducer producer;

    public Order create(CreateOrderRequest req) {
        Order order = repository.save(Order.builder()
                .productId(req.getProductId())
                .quantity(req.getQuantity())
                .customerEmail(req.getCustomerEmail())
                .status(OrderStatus.PENDING)
                .createdAt(Instant.now())
                .build());

        producer.publish(OrderEvent.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .customerEmail(order.getCustomerEmail())
                .eventType("ORDER_CREATED")
                .build());

        return order;
    }

    public List<Order> findAll() {
        return repository.findAll();
    }
}
