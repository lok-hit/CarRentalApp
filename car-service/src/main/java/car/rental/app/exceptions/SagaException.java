package car.rental.app.exceptions;

public class SagaException extends RuntimeException{

    public SagaException(String message, Throwable cause){
        super(message,cause);
    }
}
