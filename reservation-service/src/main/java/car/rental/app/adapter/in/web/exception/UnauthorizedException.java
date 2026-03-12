package car.rental.app.adapter.in.web.exception;

public class UnauthorizedException extends ApiException {

    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }
}
