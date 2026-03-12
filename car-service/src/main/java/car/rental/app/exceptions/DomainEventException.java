package car.rental.app.exceptions;

public class DomainEventException extends RuntimeException{

    public DomainEventException(String message, Throwable cause){
        super(message, cause);
    }
}
