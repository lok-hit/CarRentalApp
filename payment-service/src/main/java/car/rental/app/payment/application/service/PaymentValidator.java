package car.rental.app.payment.application.service;

import car.rental.app.payment.application.validation.PaymentValidationRules;
import car.rental.app.payment.domain.model.Money;
import org.springframework.stereotype.Component;

@Component
public class PaymentValidator {

    private final PaymentValidationRules rules;

    public PaymentValidator(PaymentValidationRules rules) {
        this.rules = rules;
    }

    public void validate(String reservationId, String customerId, Money amount) {

        rules.validateReservationId(reservationId);
        rules.validateCustomerId(customerId);
        rules.validateAmountNotNull(amount);
        rules.validateAmountGreaterThanZero(amount);
        rules.validateCurrency(amount);
    }
}
