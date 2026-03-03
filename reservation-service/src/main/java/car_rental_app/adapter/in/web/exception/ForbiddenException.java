package car_rental_app.adapter.in.web.exception;

public class ForbiddenException extends ApiException {

    public ForbiddenException(String message) {
        super(message, "FORBIDDEN");
    }
}
