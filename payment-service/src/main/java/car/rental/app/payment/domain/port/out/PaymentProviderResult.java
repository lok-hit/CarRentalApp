package car.rental.app.payment.domain.port.out;

public record PaymentProviderResult(
        boolean success,
        String failureReason
) {
    public static PaymentProviderResult ok() {
        return new PaymentProviderResult(true, null);
    }

    public static PaymentProviderResult failed(String reason) {
        return new PaymentProviderResult(false, reason);
    }
}
