package car.rental.app.payment.application.saga;

import car.rental.app.payment.application.dto.PaymentRequestDto;
import car.rental.app.payment.application.service.*;
import car.rental.app.payment.domain.command.PaymentRequested;
import car.rental.app.payment.domain.command.RefundRequested;
import car.rental.app.payment.domain.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class SagaCommandHandler {

    private final PaymentApplicationService paymentService;
    private final PaymentValidator validator;
    private final IdempotencyService idempotency;
    private final CorrelationIdService correlation;
    private final EventFactory eventFactory;
    private final PaymentAuditService audit;
    private final MetricsService metrics;

    public SagaCommandHandler(
            PaymentApplicationService paymentService,
            PaymentValidator validator,
            IdempotencyService idempotency,
            CorrelationIdService correlation,
            EventFactory eventFactory,
            PaymentAuditService audit,
            MetricsService metrics
    ) {
        this.paymentService = paymentService;
        this.validator = validator;
        this.idempotency = idempotency;
        this.correlation = correlation;
        this.eventFactory = eventFactory;
        this.audit = audit;
        this.metrics = metrics;
    }

    public void handle(PaymentRequested command) {

        correlation.bind(command.correlationId());

        if (!idempotency.tryExecute(command.idempotencyKey())) {
            return;
        }

        validator.validate(
                command.reservationId(),
                command.customerId(),
                command.amount()
        );

        audit.recordReceived(command);

        Payment payment = paymentService.processPayment(
                command.reservationId(),
                command.customerId(),
                command.amount()
        );

        audit.recordSuccess(payment);

        metrics.incrementSagaStep("payment_processed");

        eventFactory.publishPaymentCompleted(payment);
    }

    public void handle(RefundRequested command) {

        correlation.bind(command.correlationId());

        if (!idempotency.tryExecute(command.idempotencyKey())) {
            return;
        }

        audit.recordReceived(command);

        Payment refunded = paymentService.refundPayment(command.paymentId());

        audit.recordRefund(refunded);

        metrics.incrementSagaStep("payment_refunded");

        eventFactory.publishPaymentRefunded(refunded);
    }
}
