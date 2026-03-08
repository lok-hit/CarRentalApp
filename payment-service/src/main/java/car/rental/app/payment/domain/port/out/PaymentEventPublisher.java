package car.rental.app.payment.domain.port.out;

import car.rental.app.payment.domain.event.PaymentCompleted;
import car.rental.app.payment.domain.event.PaymentFailed;

public interface PaymentEventPublisher {

    void publish(PaymentCompleted event);

    void publish(PaymentFailed event);
}
