package car.rental.app.payment.application.saga;

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
    private final CommandFactory commandFactory;

    private final PaymentAuditService audit;
    private final MetricsService metrics;
    private final SagaErrorHandler errorHandler;

    public void handle(PaymentRequested command) {

        correlation.bind(command.correlationId());

        if (!idempotency.tryExecute(command.idempotencyKey())) {
            return;
        }

        try {
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

        } catch (Exception ex) {
            errorHandler.handlePaymentRequestedError(command, ex);
        }
    }

    public void handle(RefundRequested command) {

        correlation.bind(command.correlationId());

        if (!idempotency.tryExecute(command.idempotencyKey())) {
            return;
        }

        try {
            audit.recordReceived(command);

            Payment refunded = paymentService.refundPayment(command.paymentId());

            audit.recordRefund(refunded);
            metrics.incrementSagaStep("payment_refunded");
            eventFactory.publishPaymentRefunded(refunded);

        } catch (Exception ex) {
            errorHandler.handleRefundRequestedError(command, ex);
        }
    }
}