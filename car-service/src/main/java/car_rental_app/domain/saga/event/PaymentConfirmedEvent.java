package car_rental_app.domain.saga.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentConfirmedEvent {

    private final String reservationId;
    private final String carId;
    private final String userId;
    private final String paymentStatus;

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

    public String getReservationId() {
        return reservationId;
    }

    public String getCarId() {
        return carId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

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

