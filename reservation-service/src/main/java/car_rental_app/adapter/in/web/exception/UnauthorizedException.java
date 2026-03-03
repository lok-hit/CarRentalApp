package car_rental_app.adapter.in.web.exception;

public class UnauthorizedException extends ApiException {

    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }
}
