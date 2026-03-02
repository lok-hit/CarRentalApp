package car_rental_app.domain.saga.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentConfirmedEvent {

    private final String reservationId;
    private final String carId;
    private final String userId;
    private final String paymentId;

    @JsonCreator
    public PaymentConfirmedEvent(
            @JsonProperty("reservationId") String reservationId,
            @JsonProperty("carId") String carId,
            @JsonProperty("userId") String userId,
            @JsonProperty("paymentId") String paymentId
    ) {
        this.reservationId = reservationId;
        this.carId = carId;
        this.userId = userId;
        this.paymentId = paymentId;
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

    public String getPaymentId() {
        return paymentId;
    }

    @Override
    public String toString() {
        return "PaymentConfirmedEvent{" +
                "reservationId='" + reservationId + '\'' +
                ", carId='" + carId + '\'' +
                ", userId='" + userId + '\'' +
                ", paymentId='" + paymentId + '\'' +
                '}';
    }
}

