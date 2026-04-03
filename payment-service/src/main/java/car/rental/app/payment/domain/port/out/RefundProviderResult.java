package car.rental.app.payment.domain.port.out;

public record RefundProviderResult(
        boolean success,
        String failureReason,
        String requestPayload,
        String responsePayload
) {

    public static RefundProviderResult ok(String request, String response) {
        return new RefundProviderResult(true, null, request, response);
    }

    public static RefundProviderResult failed(String reason, String request, String response) {
        return new RefundProviderResult(false, reason, request, response);
    }
}

