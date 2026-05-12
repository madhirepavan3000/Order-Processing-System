# Microservices-based Order Processing System

Java 17 · Spring Boot 3 · Apache Kafka · Spring Cloud Gateway · MySQL · Docker

## Architecture

```
         ┌───────────────────┐
 client ─►  API Gateway      │  (Spring Cloud Gateway, port 8080)
         └─────────┬─────────┘
                   │ routes /orders/**, /inventory/**
       ┌───────────┼─────────────────────────┐
       ▼           ▼                         ▼
 ┌───────────┐ ┌──────────────┐      ┌──────────────────┐
 │ Order     │ │ Inventory    │      │ Notification     │
 │ Service   │ │ Service      │      │ Service          │
 │ :8081     │ │ :8082        │      │ :8083            │
 └─────┬─────┘ └──────┬───────┘      └─────────┬────────┘
       │ produces     │ consumes order-events  │ consumes
       │ order-events │ produces inventory-... │ both topics
       └──────────────┴────► Apache Kafka ◄────┘
                                  │
                              ┌───┴────┐
                              │ MySQL  │  (separate schemas per service)
                              └────────┘
```

Communication: REST (client → gateway → service) + asynchronous event streaming
between services via Kafka topics `order-events` and `inventory-events`.

## Services

| Service              | Port  | Responsibility                                      |
|----------------------|-------|-----------------------------------------------------|
| api-gateway          | 8080  | Routing, load balancing, single entry point         |
| order-service        | 8081  | Place/list orders; emits `OrderCreated` events      |
| inventory-service    | 8082  | Reserve stock on `OrderCreated`; emits reservation  |
| notification-service | 8083  | Listens to both topics, sends notifications (log)   |

## Run with Docker

```bash
docker compose up --build
```

This starts Zookeeper, Kafka, MySQL, and all four Spring Boot services.

Test:
```bash
# Place an order
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"productId":"SKU-1","quantity":2,"customerEmail":"a@b.com"}'

# List orders
curl http://localhost:8080/orders

# Inventory
curl http://localhost:8080/inventory
```

Watch `notification-service` logs to see events flowing through Kafka.

## Run locally (without Docker)

1. Start Kafka + MySQL however you like (Docker, brew, etc.).
2. `cd order-service && ./mvnw spring-boot:run` — repeat for each service.

## Tech

- Spring Boot 3.2, Spring Web, Spring Data JPA / Hibernate
- Spring Kafka
- Spring Cloud Gateway (reactive)
- MySQL 8 (one schema per service)
- Lombok
- Maven
- Docker / docker-compose
