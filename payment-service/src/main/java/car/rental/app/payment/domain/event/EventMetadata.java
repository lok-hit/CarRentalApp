package car.rental.app.payment.domain.event;

import java.time.Instant;

public record EventMetadata(
        String eventId,
        String eventType,
        String eventVersion,
        Instant occurredAt,
        String traceId,
        String correlationId
) {}
