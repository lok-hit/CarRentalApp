package car_rental_app.application.exception;

public class SagaProcessingException extends RuntimeException {

    public SagaProcessingException(String message) {
        super(message);
    }

    public SagaProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
