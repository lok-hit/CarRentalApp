package car_rental_app.domain.saga.event;

import car_rental_app.domain.event.DomainEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationConfirmedEvent extends DomainEvent {

    private final String reservationId;
    private final String carId;
    private final String userId;

    /**
     * Creates a ReservationConfirmedEvent containing identifiers for the reservation, the car, and the user.
     *
     * @param reservationId the reservation identifier
     * @param carId the car identifier
     * @param userId the user identifier
     */
    @JsonCreator
    public ReservationConfirmedEvent(@JsonProperty("reservationId") String reservationId,
                                     @JsonProperty("carId") String carId,
                                     @JsonProperty("userId") String userId) {
        this.reservationId = reservationId;
        this.carId = carId;
        this.userId = userId;
    }

    /**
     * Gets the reservation identifier associated with this event.
     *
     * @return the reservation identifier
     */
    public String getReservationId() {
        return reservationId;
    }

    /**
     * Gets the identifier of the car associated with this reservation event.
     *
     * @return the car identifier
     */
    public String getCarId() {
        return carId;
    }

    /**
     * Identifier of the user who made the reservation.
     *
     * @return the user identifier
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Provides the canonical name identifying this domain event.
     *
     * @return the event name "ReservationConfirmedEvent".
     */
    @Override
    public String eventName() {
        return "ReservationConfirmedEvent";
    }

    /**
     * Returns a string representation of this ReservationConfirmedEvent including reservationId, carId, and userId.
     *
     * @return the string in the form "ReservationConfirmedEvent{reservationId='...', carId='...', userId='...'}"
     */
    @Override
    public String toString() {
        return "ReservationConfirmedEvent{" + "reservationId='" + reservationId + '\'' + ", " +
                "carId='" + carId + '\'' + ", userId='" + userId + '\'' + '}';
    }
}

