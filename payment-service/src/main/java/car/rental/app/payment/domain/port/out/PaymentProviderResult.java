package car.rental.app.payment.domain.port.out;

public record PaymentProviderResult(
        boolean success,
        String failureReason,
        String providerPaymentId,
        String requestPayload,
        String responsePayload
) {

    public static PaymentProviderResult ok(String providerPaymentId, String request, String response) {
        return new PaymentProviderResult(true, null, providerPaymentId, request, response);
    }

    public static PaymentProviderResult failed(String reason, String request, String response) {
        return new PaymentProviderResult(false, reason, null, request, response);
    }
}

