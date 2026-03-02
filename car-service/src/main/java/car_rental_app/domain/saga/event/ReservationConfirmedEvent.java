package car_rental_app.domain.saga.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationConfirmedEvent implements DomainEvent{

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
    public String toString() {
        return "ReservationConfirmedEvent{" + "reservationId='" + reservationId + '\'' + ", " +
                "carId='" + carId + '\'' + ", userId='" + userId + '\'' + '}';
    }
}

