package car_rental_app.adapter.in.web.exception;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(message, "NOT_FOUND");
    }
}
