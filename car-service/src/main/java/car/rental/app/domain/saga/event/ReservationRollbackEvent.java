package car.rental.app.domain.saga.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationRollbackEvent {

    private final String reservationId;
    private final String carId;
    private final String userId;
    private final String reason;

    /**
     * Create a ReservationRollbackEvent with the specified reservation, car, and user identifiers and a textual reason.
     *
     * @param reservationId the reservation identifier
     * @param carId         the car identifier
     * @param userId        the user identifier
     * @param reason        the reason for the rollback
     */
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

    /**
     * Gets the reservation identifier.
     *
     * @return the reservation identifier
     */
    public String getReservationId() {
        return reservationId;
    }

    /**
     * Gets the identifier of the car associated with the reservation.
     *
     * @return the car identifier
     */
    public String getCarId() {
        return carId;
    }

    /**
     * The identifier of the user associated with the reservation.
     *
     * @return the user identifier
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Retrieves the reason for the reservation rollback event.
     *
     * @return the reason for the rollback
     */
    public String getReason() {
        return reason;
    }

    /**
     * Human-readable representation of the reservation rollback event including reservationId, carId, userId, and reason.
     *
     * @return a string containing the event's reservationId, carId, userId, and reason
     */
    @Override
    public String toString() {
        return "ReservationRollbackEvent{" + "reservationId='" + reservationId + '\'' + ", " +
                "carId='" + carId + '\'' + ", userId='" + userId + '\'' + ", " +
                "reason='" + reason + '\'' + '}';
    }
}

