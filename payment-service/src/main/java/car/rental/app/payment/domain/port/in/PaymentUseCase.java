package car.rental.app.payment.domain.port.in;

import car.rental.app.payment.domain.model.Money;
import car.rental.app.payment.domain.model.Payment;

public interface PaymentUseCase {

    Payment processPayment(String reservationId, String customerId, Money amount);
}
