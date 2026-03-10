package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.event.EventMetadata;
import car.rental.app.payment.domain.event.PaymentCompleted;
import car.rental.app.payment.domain.event.PaymentFailed;
import car.rental.app.payment.domain.model.Payment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class EventFactory {

    private final CorrelationIdService correlationIdService;

    public EventFactory(CorrelationIdService correlationIdService) {
        this.correlationIdService = correlationIdService;
    }

    public PaymentCompleted paymentCompleted(Payment payment) {
        return new PaymentCompleted(
                payment,
                metadata("PaymentCompleted", "1")
        );
    }

    public PaymentFailed paymentFailed(String paymentId, String reservationId, String reason) {
        return new PaymentFailed(
                paymentId,
                reservationId,
                reason,
                metadata("PaymentFailed", "1")
        );
    }

    private EventMetadata metadata(String eventType, String version) {
        return new EventMetadata(
                UUID.randomUUID().toString(),
                eventType,
                version,
                Instant.now(),
                correlationIdService.getOrCreateTraceId(),
                correlationIdService.getOrCreateCorrelationId()
        );
    }
}
