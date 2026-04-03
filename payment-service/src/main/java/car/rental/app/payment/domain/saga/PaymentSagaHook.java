package car.rental.app.payment.domain.saga;

import car.rental.app.payment.domain.model.Money;
import car.rental.app.payment.domain.model.Payment;

public interface PaymentSagaHook {

    Payment initiatePayment(String reservationId, String customerId, Money amount);

    Payment confirmPayment(String paymentId);

    Payment failPayment(String paymentId, String reason);
}
