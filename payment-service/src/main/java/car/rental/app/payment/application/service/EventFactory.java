package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.event.EventMetadata;
import car.rental.app.payment.domain.event.PaymentCompleted;
import car.rental.app.payment.domain.event.PaymentFailed;
import car.rental.app.payment.domain.event.RefundCompleted;
import car.rental.app.payment.domain.event.RefundFailed;
import car.rental.app.payment.domain.model.Payment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class EventFactory {

    private EventMetadata metadata(String type, String version) {
        return new EventMetadata(
                UUID.randomUUID().toString(), // eventId
                type,                         // eventType
                version,                      // eventVersion
                Instant.now(),                // occurredAt
                null,                         // traceId (podciągniesz jak masz kontekst)
                null                          // correlationId
        );
    }

    public PaymentCompleted paymentCompleted(Payment payment) {
        return new PaymentCompleted(
                payment.id(),
                payment.reservationId(),
                payment.customerId(),
                payment.amount(),
                payment.providerPaymentId(),
                payment.paidAt(),
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

    public RefundCompleted refundCompleted(Payment payment) {
        return new RefundCompleted(
                payment.id(),
                payment.reservationId(),
                payment.customerId(),
                payment.amount(),
                payment.providerPaymentId(),
                payment.paidAt(),
                metadata("RefundCompleted", "1")
        );
    }

    public RefundFailed refundFailed(String paymentId, String reservationId, String reason) {
        return new RefundFailed(
                paymentId,
                reservationId,
                reason,
                metadata("RefundFailed", "1")
        );
    }
}
