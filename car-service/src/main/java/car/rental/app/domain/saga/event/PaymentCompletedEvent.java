package car.rental.app.domain.saga.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentCompletedEvent(String reservationId, String carId, String userId, String paymentStatus) {
    /**
     * Creates a PaymentCompletedEvent with the given reservation, car, and user identifiers and the payment status.
     *
     * @param reservationId the reservation identifier
     * @param carId         the car identifier
     * @param userId        the user identifier
     * @param paymentStatus the payment status for the reservation
     */
    @JsonCreator
    public PaymentCompletedEvent(@JsonProperty("reservationId") String reservationId,
                                 @JsonProperty("carId") String carId,
                                 @JsonProperty("userId") String userId,
                                 @JsonProperty("paymentStatus")
                                 String paymentStatus) {
        this.reservationId = reservationId;
        this.carId = carId;
        this.userId = userId;
        this.paymentStatus = paymentStatus;
    }

    /**
     * Provide a concise string representation of the PaymentCompletedEvent containing its component values.
     *
     * @return a string formatted as PaymentCompletedEvent{reservationId='...', carId='...', userId='...', paymentStatus='...'}
     */
    @Override
    public String toString() {
        return "PaymentCompletedEvent{" + "reservationId='" + reservationId + '\'' + ", carId='" + carId + '\'' + ", userId='" + userId + '\'' + ", paymentStatus='" + paymentStatus + '\'' + '}';
    }
}
