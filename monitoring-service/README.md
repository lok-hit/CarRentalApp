Monitoring Service
The Monitoring Service collects audit events, correlates traces, and exposes metrics for observability. It is the central analytics and compliance component of the platform.

Responsibilities
Receive audit events from API Gateway

Store structured logs

Correlate traceId, spanId, correlationId

Expose Prometheus metrics

Integrate with OpenTelemetry Collector

Provide dashboards (Grafana)

Features
Reactive WebFlux API

High‑volume event ingestion

JSON structured logging

Trace correlation

Actuator metrics