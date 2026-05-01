package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.event.EventMetadata;
import car.rental.app.payment.domain.event.PaymentCompleted;
import car.rental.app.payment.domain.event.PaymentFailed;
import car.rental.app.payment.domain.event.RefundCompleted;
import car.rental.app.payment.domain.event.RefundFailed;
import car.rental.app.payment.domain.model.Payment;
import car.rental.app.payment.domain.port.out.PaymentEventPublisher;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class EventFactory {

    private final PaymentEventPublisher eventPublisher;
    private final CorrelationIdService correlationIdService;

    public EventFactory(PaymentEventPublisher eventPublisher, CorrelationIdService correlationIdService) {
        this.eventPublisher = eventPublisher;
        this.correlationIdService = correlationIdService;
    }

    private EventMetadata metadata(String type, String version) {
        return new EventMetadata(
                UUID.randomUUID().toString(),
                type,
                version,
                Instant.now(),
                correlationIdService.getOrCreateTraceId(),
                correlationIdService.getOrCreateCorrelationId()
        );
    }

    public void publishPaymentCompleted(Payment payment) {
        PaymentCompleted event = new PaymentCompleted(
                payment.id(),
                payment.reservationId(),
                payment.customerId(),
                payment.amount(),
                payment.providerPaymentId(),
                payment.paidAt(),
                metadata("PaymentCompleted", "1")
        );
        eventPublisher.publish(event);
    }

    public void publishPaymentRefunded(Payment payment) {
        RefundCompleted event = new RefundCompleted(
                payment.id(),
                payment.reservationId(),
                payment.customerId(),
                payment.amount(),
                payment.providerPaymentId(),
                payment.paidAt(),
                metadata("RefundCompleted", "1")
        );
        eventPublisher.publish(event);
    }

    public void publishPaymentFailed(Payment payment, String reason) {
        PaymentFailed event = new PaymentFailed(
                payment.id(),
                payment.reservationId(),
                reason,
                metadata("PaymentFailed", "1")
        );
        eventPublisher.publish(event);
    }

    public void publishPaymentFailed(String reservationId, String reason) {
        PaymentFailed event = new PaymentFailed(
                null,
                reservationId,
                reason,
                metadata("PaymentFailed", "1")
        );
        eventPublisher.publish(event);
    }

    public void publishRefundFailed(String paymentId, String reason) {
        RefundFailed event = new RefundFailed(
                paymentId,
                null,
                reason,
                metadata("RefundFailed", "1")
        );
        eventPublisher.publish(event);
    }
}
