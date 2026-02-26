Architecture Overview
The gateway acts as a reverse proxy and request orchestrator. It performs:

Authentication and authorization (JWT-based)

Request logging and correlation

Distributed tracing and metrics export

Audit event publishing

Routing and load balancing to backend services

Error handling and resilience

The architecture is designed for Kubernetes, Prometheus, Grafana, and OpenTelemetry Collector.

Key Components
1. Spring Cloud Gateway
   The gateway uses Spring Cloud Gateway as the reactive routing engine. It provides:

Non-blocking request handling (Reactor Netty)

Route definitions for backend services

Global filters for cross-cutting logic

Built-in support for rate limiting, retries, and circuit breakers

2. Reactive WebClient
   Outbound calls (e.g., audit events) use WebClient with:

Connection pooling

Timeouts

Metrics instrumentation

Tracing propagation

This ensures consistent performance and observability across all external calls.

3. Observability Stack
   The gateway includes full production-grade observability:

Distributed Tracing
OpenTelemetry instrumentation

100% sampling (configurable)

Trace propagation across all services

Export to OTLP collector

Metrics
JVM, system, and process metrics

WebFlux and WebClient metrics

Gateway route metrics

Prometheus endpoint (/actuator/prometheus)

Structured Logging
Each log entry includes:

traceId

spanId

correlationId

userId (if authenticated)

HTTP method, path, status, latency

This enables full log–trace correlation.

4. Health, Liveness, and Readiness Probes
   The gateway exposes Kubernetes‑ready health endpoints:

/actuator/health – full health

/actuator/health/liveness – JVM liveness

/actuator/health/readiness – readiness for traffic

These ensure safe rollout, autoscaling, and self‑healing.

5. Correlation and Logging Filters
   Two custom filters ensure consistent request tracking:

CorrelationIdFilter
Generates a correlation ID if missing

Propagates it through the request lifecycle

Adds it to response headers

LoggingFilter
Publishes request events

Logs structured request metadata

Enriches logs with correlation and trace IDs

6. Audit Service Integration
   The gateway sends audit events to the monitoring service using a dedicated AuditService.
   Events include:

Timestamp

User ID and roles

HTTP method and path

Status code and latency

Correlation ID and trace ID

Source system identifier

This enables full compliance and traceability.

7. Configuration (application.yml)
   The gateway includes a complete production configuration:

Actuator endpoints

OpenTelemetry exporters

Micrometer metrics

Logging patterns

WebClient tuning

Monitoring-service URL

Gateway routing and filters

This configuration ensures the gateway is fully observable, debuggable, and ready for cloud deployment.

8. Branching Strategy
   The repository uses:

develop — main integration branch

feature/* — feature development

main — stable releases

The gateway code resides under the ApiGateway module.

9. Technology Stack
   Java 17

Spring Boot 3

Spring Cloud Gateway

Spring WebFlux

Micrometer

OpenTelemetry

Prometheus / Grafana

Docker / Kubernetes