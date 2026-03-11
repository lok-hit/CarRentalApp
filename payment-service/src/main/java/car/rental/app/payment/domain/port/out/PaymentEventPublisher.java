package car.rental.app.payment.domain.port.out;

import car.rental.app.payment.domain.event.PaymentCompleted;
import car.rental.app.payment.domain.event.PaymentFailed;
import car.rental.app.payment.domain.event.RefundCompleted;
import car.rental.app.payment.domain.event.RefundFailed;

public interface PaymentEventPublisher {

    void publish(PaymentCompleted event);

    void publish(PaymentFailed event);

    void publish(RefundCompleted event);

    void publish(RefundFailed event);
}
