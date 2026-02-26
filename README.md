CarRentalApp – Microservices Platform
CarRentalApp is a cloud‑native, microservices‑based platform designed for scalable vehicle rental operations. The system follows a modular architecture with independent services communicating through REST, service discovery, and distributed tracing. The platform is optimized for Kubernetes, observability, CI/CD automation, and high availability.

Architecture Overview
The platform consists of the following core components:

API Gateway — unified entry point, routing, security, rate limiting, tracing.

Eureka Server — service discovery and dynamic registry.

Monitoring Service — audit logging, metrics aggregation, trace correlation.

Car Service — vehicle catalog, availability, pricing.

Reservation Service — booking lifecycle, availability checks, cancellations.

Payment Service — payment processing, refunds, external provider integration.

User Profile Service — user data, preferences, history.

Notification Service — email/SMS/push notifications.

Authorization Service (Keycloak) — identity, roles, tokens, OAuth2/OIDC.

All services are built with Spring Boot 3, Spring Cloud 2023, Java 21, and follow reactive, non‑blocking patterns where applicable.

Key Platform Features
Service Discovery
Eureka Server provides dynamic registration and lookup for all microservices.

API Gateway
Handles routing, authentication, rate limiting, circuit breakers, canary deployments, and distributed tracing.

Observability
OpenTelemetry tracing (OTLP)

Micrometer metrics

Prometheus scraping

Grafana dashboards

Structured logging with correlation IDs

Resilience
Resilience4j circuit breakers

Redis rate limiting

Canary routing for progressive deployments

Security
Keycloak for identity and access management

JWT validation at the gateway

Role‑based access control

CI/CD
GitHub Actions for continuous integration

Jenkins pipeline for container builds and deployments

Docker images for all services

Project Structure
Kod
CarRentalApp/
│
├── api-gateway/
├── eureka-server/
├── monitoring-service/
├── car-service/
├── reservation-service/
├── payment-service/
├── user-profile-service/
├── notification-service/
└── pom.xml  (parent)
Technology Stack
Java 17

Spring Boot 3

Spring Cloud 2023

Spring Cloud Gateway

Eureka Server

Resilience4j

Redis

OpenTelemetry

Prometheus / Grafana

Docker

Jenkins

GitHub Actions

Keycloak

Build & Run
Build all modules:
Kod
mvn clean install
Run locally (example):
Kod
cd eureka-server
mvn spring-boot:run