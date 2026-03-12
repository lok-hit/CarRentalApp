package car.rental.app.adapter.in.web.exception;

public class ForbiddenException extends ApiException {

    public ForbiddenException(String message) {
        super(message, "FORBIDDEN");
    }
}
