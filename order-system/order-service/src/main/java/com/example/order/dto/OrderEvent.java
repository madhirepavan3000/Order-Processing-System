package com.example.order.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderEvent {
    private Long orderId;
    private String productId;
    private Integer quantity;
    private String customerEmail;
    private String eventType; // ORDER_CREATED
}
