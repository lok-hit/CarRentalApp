package car.rental.app.payment.adapter.out.provider;

public class StripeException extends RuntimeException{

    public StripeException (String message, Throwable cause){
        super(message,cause);
    }
}
