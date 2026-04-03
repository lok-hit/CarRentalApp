package car.rental.app.adapter.in.web.exception;

public class ValidationException extends ApiException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}
