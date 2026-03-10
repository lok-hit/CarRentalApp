package car.rental.app.payment.domain.port.out;

public record PaymentProviderResult(
        boolean success,
        String failureReason,
        String requestPayload,
        String responsePayload
) {
    public static PaymentProviderResult ok(String request, String response) {
        return new PaymentProviderResult(true, null, request, response);
    }

    public static PaymentProviderResult failed(String reason, String request, String response) {
        return new PaymentProviderResult(false, reason, request, response);
    }
}
