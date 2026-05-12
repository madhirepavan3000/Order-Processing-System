package com.example.inventory.kafka;

import com.example.inventory.dto.OrderEvent;
import com.example.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final InventoryService service;

    @KafkaListener(topics = "${app.topics.order-events}", groupId = "inventory-service")
    public void onOrder(OrderEvent event) {
        service.handleOrder(event);
    }
}
