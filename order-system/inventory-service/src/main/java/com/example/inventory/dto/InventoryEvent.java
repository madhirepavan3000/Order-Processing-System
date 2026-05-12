package com.example.inventory.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryEvent {
    private Long orderId;
    private String productId;
    private String customerEmail;
    private String status; // RESERVED or OUT_OF_STOCK
}
