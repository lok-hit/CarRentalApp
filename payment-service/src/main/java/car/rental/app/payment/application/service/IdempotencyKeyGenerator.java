package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.model.Money;
import org.springframework.stereotype.Component;

@Component
public class IdempotencyKeyGenerator {

    public String generate(String reservationId, String customerId, Money amount) {
        return reservationId + ":" + customerId + ":" + amount.amount() + ":" + amount.currency();
    }

    public String generate(String paymentId) {
        return "refund:" + paymentId;
    }
}

