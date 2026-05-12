package com.example.notification.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationConsumer {

    @KafkaListener(
            topics = {"${app.topics.order-events}", "${app.topics.inventory-events}"},
            groupId = "notification-service",
            properties = { JsonDeserializer.VALUE_DEFAULT_TYPE + "=com.fasterxml.jackson.databind.JsonNode" }
    )
    public void onEvent(JsonNode event) {
        log.info("📧 NOTIFICATION: received event => {}", event);
        // In a real system: send email/SMS/push via SendGrid, Twilio, etc.
    }
}
