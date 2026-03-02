package car_rental_app.domain.saga.event;

import car_rental_app.domain.event.DomainEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationConfirmedEvent extends DomainEvent {

    private final String reservationId;
    private final String carId;
    private final String userId;

    @JsonCreator
    public ReservationConfirmedEvent(@JsonProperty("reservationId") String reservationId,
                                     @JsonProperty("carId") String carId,
                                     @JsonProperty("userId") String userId) {
        this.reservationId = reservationId;
        this.carId = carId;
        this.userId = userId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getCarId() {
        return carId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String eventName() {
        return "ReservationConfirmedEvent";
    }

    @Override
    public String toString() {
        return "ReservationConfirmedEvent{" + "reservationId='" + reservationId + '\'' + ", " +
                "carId='" + carId + '\'' + ", userId='" + userId + '\'' + '}';
    }
}

