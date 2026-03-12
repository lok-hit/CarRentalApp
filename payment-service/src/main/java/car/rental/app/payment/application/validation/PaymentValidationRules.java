package car.rental.app.payment.application.validation;

import car.rental.app.payment.application.InvalidPaymentException;
import car.rental.app.payment.domain.model.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;

@Component
public class PaymentValidationRules {

    public void validateReservationId(String id) {
        require(id != null && !id.isBlank(), "Reservation ID cannot be empty");
    }

    public void validateCustomerId(String id) {
        require(id != null && !id.isBlank(), "Customer ID cannot be empty");
    }

    public void validateAmountNotNull(Money money) {
        require(money != null, "Amount cannot be null");
        require(money.amount() != null, "Amount value cannot be null");
    }

    public void validateAmountGreaterThanZero(Money money) {
        require(money.amount().compareTo(BigDecimal.ZERO) > 0,
                "Amount must be greater than zero");
    }

    public void validateCurrency(Money money) {
        Currency currency = money.currency();
        require(currency != null, "Currency cannot be null");
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new InvalidPaymentException(message);
        }
    }
}
