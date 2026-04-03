package car.rental.app.monitoring.dto;

import java.time.Instant;

public record AuditEventRequest(
        String eventType,
        String serviceName,
        String traceId,
        String spanId,
        String correlationId,
        String payload,
        Instant occurredAt
) {}
