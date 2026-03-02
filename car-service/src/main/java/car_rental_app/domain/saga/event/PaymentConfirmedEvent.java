package car_rental_app.domain.saga.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentConfirmedEvent {

    private final String reservationId;
    private final String carId;
    private final String userId;
    private final String paymentStatus;

    /**
     * Constructs a PaymentConfirmedEvent with the given reservation, car, and user identifiers and the payment status.
     *
     * @param reservationId the reservation identifier
     * @param carId the car identifier
     * @param userId the user identifier
     * @param paymentStatus the payment status string
     */
    @JsonCreator
    public PaymentConfirmedEvent(
            @JsonProperty("reservationId") String reservationId,
            @JsonProperty("carId") String carId,
            @JsonProperty("userId") String userId,
            @JsonProperty("paymentStatus") String paymentStatus
    ) {
        this.reservationId = reservationId;
        this.carId = carId;
        this.userId = userId;
        this.paymentStatus = paymentStatus;
    }

    /**
     * Reservation identifier associated with this payment-confirmed event.
     *
     * @return the reservation identifier
     */
    public String getReservationId() {
        return reservationId;
    }

    /**
     * Gets the car identifier associated with this payment confirmation.
     *
     * @return the car identifier
     */
    public String getCarId() {
        return carId;
    }

    /**
     * Retrieves the user identifier associated with this payment event.
     *
     * @return the user identifier for the reservation/payment
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Payment status of the confirmed payment.
     *
     * @return the payment status as a {@code String}
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Provide a human-readable representation of this payment confirmation event.
     *
     * @return a string containing the class name and the values of `reservationId`, `carId`, `userId`, and `paymentStatus`
     */
    @Override
    public String toString() {
        return "PaymentConfirmedEvent{" +
                "reservationId='" + reservationId + '\'' +
                ", carId='" + carId + '\'' +
                ", userId='" + userId + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}

