package car.rental.app.payment.application.saga;

import car.rental.app.payment.application.service.EventFactory;
import car.rental.app.payment.application.service.MetricsService;
import car.rental.app.payment.application.service.PaymentAuditService;
import car.rental.app.payment.domain.command.PaymentRequested;
import car.rental.app.payment.domain.command.RefundRequested;
import car.rental.app.payment.domain.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SagaErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(SagaErrorHandler.class);

    private final PaymentAuditService audit;
    private final MetricsService metrics;
    private final EventFactory eventFactory;

    public SagaErrorHandler(
            PaymentAuditService audit,
            MetricsService metrics,
            EventFactory eventFactory
    ) {
        this.audit = audit;
        this.metrics = metrics;
        this.eventFactory = eventFactory;
    }

    public void handlePaymentRequestedError(PaymentRequested command, Exception ex) {
        log.error("Error while processing PaymentRequested saga step, reservationId={}", command.reservationId(), ex);

        audit.recordFailure(command, ex);
        metrics.incrementSagaStep("payment_error");
        eventFactory.publishPaymentFailed(command.reservationId(), ex.getMessage());
    }

    public void handleRefundRequestedError(RefundRequested command, Exception ex) {
        log.error("Error while processing RefundRequested saga step, paymentId={}", command.paymentId(), ex);

        audit.recordFailure(command, ex);
        metrics.incrementSagaStep("refund_error");
        eventFactory.publishRefundFailed(command.paymentId(), ex.getMessage());
    }

    public void handlePaymentDomainError(Payment payment, Exception ex) {
        log.error("Error after payment was created, paymentId={}", payment.id(), ex);

        audit.recordFailure(payment, ex);
        metrics.incrementSagaStep("payment_domain_error");

        // tu już masz Payment, więc możesz spokojnie wysłać PaymentFailedEvent
        eventFactory.publishPaymentFailed(payment, ex.getMessage());
    }
}
