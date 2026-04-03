package car.rental.app.payment.application.saga;

import car.rental.app.payment.application.service.CorrelationIdService;
import car.rental.app.payment.application.service.IdempotencyKeyGenerator;
import car.rental.app.payment.domain.command.PaymentRequested;
import car.rental.app.payment.domain.command.RefundRequested;
import car.rental.app.payment.domain.model.Money;
import org.springframework.stereotype.Component;

@Component
public class CommandFactory {

    private final CorrelationIdService correlationIdService;
    private final IdempotencyKeyGenerator idempotencyKeyGenerator;

    public CommandFactory(
            CorrelationIdService correlationIdService,
            IdempotencyKeyGenerator idempotencyKeyGenerator
    ) {
        this.correlationIdService = correlationIdService;
        this.idempotencyKeyGenerator = idempotencyKeyGenerator;
    }

    public PaymentRequested createPaymentRequested(
            String reservationId,
            String customerId,
            Money amount
    ) {
        return new PaymentRequested(
                reservationId,
                customerId,
                amount,
                correlationIdService.current(),
                idempotencyKeyGenerator.generate(reservationId, customerId, amount)
        );
    }

    public RefundRequested createRefundRequested(String paymentId) {
        return new RefundRequested(
                paymentId,
                correlationIdService.current(),
                idempotencyKeyGenerator.generate(paymentId)
        );
    }
}
