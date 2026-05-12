package com.example.order.kafka;

import com.example.order.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.topics.order-events}")
    private String topic;

    public void publish(OrderEvent event) {
        log.info("Publishing OrderEvent to {}: {}", topic, event);
        kafkaTemplate.send(topic, String.valueOf(event.getOrderId()), event);
    }
}
