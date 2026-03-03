package car_rental_app.application.exception;

public class ReservationAlreadyConfirmedException extends InvalidReservationStateException {

    public ReservationAlreadyConfirmedException(String reservationId) {
        super("Reservation already confirmed: " + reservationId);
    }
}
