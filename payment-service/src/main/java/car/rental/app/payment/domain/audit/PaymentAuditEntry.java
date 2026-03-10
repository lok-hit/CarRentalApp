package car.rental.app.payment.domain.audit;

import java.time.Instant;

public record PaymentAuditEntry(
        String paymentId,
        String reservationId,
        String customerId,
        String provider,
        String requestPayload,
        String responsePayload,
        boolean success,
        String failureReason,
        Instant occurredAt,
        String traceId,
        String correlationId
) {}
