package car_rental_app.domain.saga.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationRollbackEvent {

    private final String reservationId;
    private final String carId;
    private final String userId;
    private final String reason;

    @JsonCreator
    public ReservationRollbackEvent(@JsonProperty("reservationId") String reservationId,
                                    @JsonProperty("carId") String carId,
                                    @JsonProperty("userId") String userId,
                                    @JsonProperty("reason") String reason) {
        this.reservationId = reservationId;
        this.carId = carId;
        this.userId = userId;
        this.reason = reason;
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

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "ReservationRollbackEvent{" + "reservationId='" + reservationId + '\'' + ", " +
                "carId='" + carId + '\'' + ", userId='" + userId + '\'' + ", " +
                "reason='" + reason + '\'' + '}';
    }
}

