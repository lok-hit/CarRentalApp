package car.rental.app.payment.application;

public class InvalidPaymentException extends RuntimeException{

    public InvalidPaymentException(String message){
        super(message);
    }

    public InvalidPaymentException(String message, Throwable cause){
        super(message, cause);
    }
}
