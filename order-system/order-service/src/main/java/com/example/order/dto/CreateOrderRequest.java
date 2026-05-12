package com.example.order.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String productId;
    private Integer quantity;
    private String customerEmail;
}
