API Gateway
The API Gateway is the central entry point for all external traffic in the CarRentalApp platform. It provides routing, authentication, observability, resilience, and cross‑cutting concerns for all backend microservices.

Core Responsibilities
Routing to backend services via Eureka discovery

JWT authentication and authorization

Rate limiting (Redis)

Circuit breakers (Resilience4j)

Canary deployments (weighted routing)

Distributed tracing (OpenTelemetry)

Metrics and health endpoints (Actuator)

Structured logging with correlation IDs

Audit event publishing to Monitoring Service

Key Features
Service Discovery Integration
The gateway uses Eureka to dynamically resolve service instances:

Kod
uri: lb://car-service
Rate Limiting
Token‑bucket rate limiting using Redis:

Per‑user or per‑IP throttling

Burst capacity control

429 responses when limits exceeded

Circuit Breakers
Resilience4j protects backend services from cascading failures:

Failure rate thresholds

Slow call detection

Fallback routes

Canary Deployments
Weighted routing allows progressive rollout:

Kod
Weight=group-cars, 90
Weight=group-cars, 10
Observability
OpenTelemetry tracing with 100% sampling

Prometheus metrics

WebFlux/WebClient instrumentation

Structured logs with traceId/spanId/correlationId

Endpoints
/actuator/health

/actuator/metrics

/actuator/prometheus

/actuator/loggers

Docker
Kod
docker build -t api-gateway .
docker run -p 8080:8080 api-gateway
CI/CD
GitHub Actions workflow for build & test

Jenkins pipeline for Docker image build & push

Configuration
All configuration is located in:

Kod
src/main/resources/application.yml