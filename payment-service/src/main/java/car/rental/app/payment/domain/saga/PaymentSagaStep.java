package car.rental.app.payment.domain.saga;

public enum PaymentSagaStep {
    PAYMENT_INITIATE,
    PAYMENT_CONFIRM,
    PAYMENT_FAIL,
    PAYMENT_REFUND
}
