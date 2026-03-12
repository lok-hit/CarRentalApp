package car.rental.app.adapter.in.web.exception;

public class ConflictException extends ApiException {

    public ConflictException(String message) {
        super(message, "CONFLICT");
    }
}
