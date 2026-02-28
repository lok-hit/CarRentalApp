package car_rental_app.application.service.saga.event;

import java.time.Instant;

public record PaymentFailedEvent(String paymentId, String reservationId, String carId, String reason, Instant timestamp ) {}
