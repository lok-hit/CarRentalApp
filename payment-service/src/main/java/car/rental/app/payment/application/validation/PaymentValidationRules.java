package car.rental.app.payment.application.validation;

import car.rental.app.payment.application.InvalidPaymentException;
import car.rental.app.payment.domain.model.Money;

import java.math.BigDecimal;
import java.util.Currency;

public class PaymentValidationRules {

    public static ValidationRule<String> reservationIdNotEmpty =
            id -> require(id != null && !id.isBlank(),
                    "Reservation ID cannot be empty");

    public static ValidationRule<String> customerIdNotEmpty =
            id -> require(id != null && !id.isBlank(), "Customer ID cannot be empty");

    public static ValidationRule<Money> amountNotNull =
            money -> require(money != null && money.amount() != null, "Amount cannot be null");

    public static ValidationRule<Money> amountGreaterThanZero =
            money -> require(money.amount().compareTo(BigDecimal.ZERO) > 0, "Amount must be greater than zero");

    public static ValidationRule<Money> currencyValid =
            money -> require(isValidCurrency(money.currency()), "Invalid currency: " + money.currency());

    private static boolean isValidCurrency(Currency currency) {
        return currency != null;
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new InvalidPaymentException(message);
        }
    }
}
