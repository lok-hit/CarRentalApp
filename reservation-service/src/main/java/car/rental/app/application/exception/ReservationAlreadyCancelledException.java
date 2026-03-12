package car.rental.app.application.exception;

public class ReservationAlreadyCancelledException extends InvalidReservationStateException {

    public ReservationAlreadyCancelledException(String reservationId) {
        super("Reservation already cancelled: " + reservationId);
    }
}
