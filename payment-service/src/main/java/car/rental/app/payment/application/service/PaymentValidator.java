package car.rental.app.payment.application.service;

import car.rental.app.payment.application.InvalidPaymentException;
import car.rental.app.payment.application.validation.PaymentValidationRules;
import car.rental.app.payment.application.validation.ValidationRule;
import car.rental.app.payment.domain.model.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
@Component
public class PaymentValidator {

    public void validate(String reservationId, String customerId, Money amount) {

        List.of(
                (Runnable) () -> PaymentValidationRules.reservationIdNotEmpty.validate(reservationId),
                (Runnable) () -> PaymentValidationRules.customerIdNotEmpty.validate(customerId),
                (Runnable) () -> PaymentValidationRules.amountNotNull.validate(amount),
                (Runnable) () -> PaymentValidationRules.amountGreaterThanZero.validate(amount),
                (Runnable) () -> PaymentValidationRules.currencyValid.validate(amount)
        ).forEach(Runnable::run);
    }
}