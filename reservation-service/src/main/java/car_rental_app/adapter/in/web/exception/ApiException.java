package car_rental_app.adapter.in.web.exception;

public abstract class ApiException extends RuntimeException {

    private final String errorCode;

    protected ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String errorCode() {
        return errorCode;
    }
}
