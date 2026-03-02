package car_rental_app.application.service.exception;

public class CarReservationRejectedException extends RuntimeException{

    /**
     * Creates a CarReservationRejectedException with the specified detail message.
     *
     * @param message the detail message describing why the reservation was rejected
     */
    public CarReservationRejectedException(String message){
        super(message);
    }
}
