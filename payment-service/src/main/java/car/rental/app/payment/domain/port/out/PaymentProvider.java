package car.rental.app.payment.domain.port.out;

import car.rental.app.payment.domain.model.Payment;

public interface PaymentProvider {

    PaymentProviderResult charge(Payment payment);

    RefundProviderResult refund(Payment payment);
}

