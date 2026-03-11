package car.rental.app.payment.application.saga;

import car.rental.app.payment.application.service.PaymentApplicationService;
import car.rental.app.payment.domain.command.PaymentRequested;
import car.rental.app.payment.domain.command.RefundRequested;
import org.springframework.stereotype.Component;

@Component
public class SagaCommandHandler {

    private final PaymentApplicationService paymentService;

    public SagaCommandHandler(PaymentApplicationService paymentService) {
        this.paymentService = paymentService;
    }

    public void handle(PaymentRequested command) {
        paymentService.processPayment(
                command.reservationId(),
                command.customerId(),
                command.amount()
        );
    }

    public void handle(RefundRequested command) {
        paymentService.refundPayment(command.paymentId());
    }
}