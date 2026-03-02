package car_rental_app.domain.saga.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentCompletedEvent(String reservationId, String carId, String userId, String paymentStatus) {
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

    @Override
    public String toString() {
        return "PaymentCompletedEvent{" + "reservationId='" + reservationId + '\'' + ", carId='" + carId + '\'' + ", userId='" + userId + '\'' + ", paymentStatus='" + paymentStatus + '\'' + '}';
    }
}
