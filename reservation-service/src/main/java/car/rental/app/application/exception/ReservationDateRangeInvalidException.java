package car.rental.app.application.exception;

public class ReservationDateRangeInvalidException extends RuntimeException {

    public ReservationDateRangeInvalidException(String message) {
        super(message);
    }
}
