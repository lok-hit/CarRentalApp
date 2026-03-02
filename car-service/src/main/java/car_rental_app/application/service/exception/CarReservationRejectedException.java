package car_rental_app.application.service.exception;

public class CarReservationRejectedException extends RuntimeException{

    public CarReservationRejectedException(String message){
        super(message);
    }
}
