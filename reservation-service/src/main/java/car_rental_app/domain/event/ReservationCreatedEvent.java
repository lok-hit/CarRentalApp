package car_rental_app.domain.event;

import java.math.BigDecimal;
import java.util.Objects;

public record ReservationCreatedEvent(
        String reservationId,
        String carId,
        String customerId,
        String price,
        String eventType
) {
    public ReservationCreatedEvent {
        validateNonBlank(reservationId, "reservationId");
        validateNonBlank(carId, "carId");
        validateNonBlank(customerId, "customerId");
        validateNonBlank(eventType, "eventType");
        validatePrice(price);
    }

    public ReservationCreatedEvent(String reservationId, String carId, String customerId, String price) {
        this(reservationId, carId, customerId, price, "ReservationCreatedEvent");
    }

    private static void validateNonBlank(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }

    private static void validatePrice(String price) {
        Objects.requireNonNull(price, "price cannot be null");
        try {
            BigDecimal value = new BigDecimal(price);
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("price must be greater than zero");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("price must be a valid decimal number", e);
        }
    }
}
