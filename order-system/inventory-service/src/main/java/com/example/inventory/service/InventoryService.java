package com.example.inventory.service;

import com.example.inventory.dto.InventoryEvent;
import com.example.inventory.dto.OrderEvent;
import com.example.inventory.model.InventoryItem;
import com.example.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.topics.inventory-events}")
    private String inventoryTopic;

    @Transactional
    public void handleOrder(OrderEvent event) {
        InventoryItem item = repository.findById(event.getProductId())
                .orElseGet(() -> repository.save(InventoryItem.builder()
                        .productId(event.getProductId())
                        .availableQuantity(100) // seed default stock
                        .build()));

        String status;
        if (item.getAvailableQuantity() >= event.getQuantity()) {
            item.setAvailableQuantity(item.getAvailableQuantity() - event.getQuantity());
            repository.save(item);
            status = "RESERVED";
            log.info("Reserved {} of {} for order {}", event.getQuantity(), event.getProductId(), event.getOrderId());
        } else {
            status = "OUT_OF_STOCK";
            log.warn("Out of stock for order {}", event.getOrderId());
        }

        kafkaTemplate.send(inventoryTopic, String.valueOf(event.getOrderId()),
                InventoryEvent.builder()
                        .orderId(event.getOrderId())
                        .productId(event.getProductId())
                        .customerEmail(event.getCustomerEmail())
                        .status(status)
                        .build());
    }

    public List<InventoryItem> findAll() {
        return repository.findAll();
    }
}
