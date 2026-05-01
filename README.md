# CarRentalApp - Microservices Platform

CarRentalApp is a cloud-native, microservices-based platform designed for scalable vehicle rental operations. The system follows a modular architecture with independent services communicating through REST, Kafka messaging, service discovery, and distributed tracing. The platform is optimized for Kubernetes, observability, CI/CD automation, and high availability.

## Architecture Overview

```mermaid
graph TD
    Client([Client]) --> GW[API Gateway :8080]

    GW --> CS[Car Service :8080]
    GW --> RS[Reservation Service]
    GW --> PS[Payment Service :8082]
    GW --> UPS[User Profile Service :8087]
    GW --> NS[Notification Service :8086]
    GW --> MS[Monitoring Service :8085]

    CS & RS & PS & UPS & NS & MS --> EUR[Eureka Server :8761]

    RS -->|Kafka PaymentRequested| PS
    PS -->|Kafka PaymentCompleted/Failed| RS
    RS -->|Outbox Processor| NS

    CS --> MongoDB[(MongoDB)]
    CS --> PG[(PostgreSQL)]
    PS --> MongoDB
    UPS --> MongoDB
    MS --> MongoDB

    GW --> Redis[(Redis rate limiting)]
    GW --> KC[Keycloak :9090]

    MS --> OTEL[OpenTelemetry Collector]
    OTEL --> Prom[Prometheus]
    Prom --> Grafana[Grafana :3000]
    OTEL --> Zipkin[Zipkin :9411]
```

## Payment Saga Flow

```mermaid
sequenceDiagram
    participant RS as Reservation Service
    participant Kafka
    participant PS as Payment Service
    participant NS as Notification Service

    RS->>Kafka: PaymentRequested (correlationId, idempotencyKey)
    Kafka->>PS: SagaCommandHandler.handle(PaymentRequested)
    PS->>PS: idempotency check
    PS->>PS: validate + processPayment()
    PS->>PS: audit.recordSuccess()

    alt Payment OK
        PS->>Kafka: PaymentCompleted
        Kafka->>RS: mark reservation CONFIRMED
        RS->>NS: NotificationOutboxProcessor -> reservation-confirmed
    else Payment Failed
        PS->>Kafka: PaymentFailed
        Kafka->>RS: mark reservation CANCELLED
        RS->>NS: NotificationOutboxProcessor -> reservation-cancelled
    end
```

## Key Platform Features

### Service Discovery
Eureka Server provides dynamic registration and lookup for all microservices.

### API Gateway
Handles routing, authentication, Redis rate limiting, Resilience4j circuit breakers, canary deployments, and distributed tracing.

### Observability
- OpenTelemetry tracing (OTLP)
- Micrometer metrics
- Prometheus scraping
- Grafana dashboards
- Structured logging with correlation IDs

### Resilience
- Resilience4j circuit breakers
- Redis rate limiting
- Canary routing for progressive deployments

### Security
- Keycloak for identity and access management
- JWT validation at the gateway
- Role-based access control

### CI/CD
- GitHub Actions CI for every service (build, test, Docker image)
- Jenkins pipeline for container builds and deployments
- Docker multi-stage images for all services

## Project Structure

```
CarRentalApp/
|
+-- api-gateway/          # Spring Cloud Gateway, Redis, Resilience4j
+-- eureka-server/        # Service discovery
+-- car-service/          # Hexagonal arch, CQRS, Outbox, dual persistence (JPA+MongoDB)
+-- reservation-service/  # Saga orchestration, BDD Cucumber, NotificationOutbox
+-- payment-service/      # Stripe, idempotency, audit, Saga
+-- user-profile-service/ # OAuth2/Keycloak, MongoDB
+-- notification-service/ # Email/SMS/push notifications
+-- monitoring-service/   # WebFlux, reactive MongoDB, audit events
+-- k8s/                  # Kubernetes manifests
+-- charts/               # Helm chart
+-- docker-compose.yml
+-- Makefile
\-- pom.xml               # Parent POM
```

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3, Spring Cloud 2023 |
| API | Spring Cloud Gateway, Spring MVC, Spring WebFlux |
| Messaging | Apache Kafka |
| Persistence | Spring Data JPA, Spring Data MongoDB |
| Security | Keycloak, OAuth2, JWT |
| Service Discovery | Eureka Server |
| Resilience | Resilience4j |
| Caching / Rate Limiting | Redis |
| Observability | OpenTelemetry, Micrometer, Prometheus, Grafana, Zipkin |
| Testing | JUnit 5, Mockito, Testcontainers, Cucumber (BDD) |
| Containerisation | Docker, Docker Compose |
| Orchestration | Kubernetes, Helm |
| CI/CD | GitHub Actions, Jenkins |

## Build & Run

Build all modules:
```bash
mvn clean install
```

Start the full platform locally (Docker Compose):
```bash
make up
```

Run a single service with the OpenTelemetry agent:
```bash
make run SERVICE=car-service
```

## API Documentation

Each service exposes Swagger UI at `/swagger-ui.html`:

| Service | URL |
|---|---|
| Car Service | http://localhost:8080/swagger-ui.html |
| Payment Service | http://localhost:8082/swagger-ui.html |
| User Profile Service | http://localhost:8087/swagger-ui.html |
| Notification Service | http://localhost:8086/swagger-ui.html |
| Monitoring Service | http://localhost:8085/swagger-ui.html |
