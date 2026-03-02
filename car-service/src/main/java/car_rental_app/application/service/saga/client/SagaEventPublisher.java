package car_rental_app.application.service.saga.client;



import car_rental_app.domain.saga.event.PaymentFailedEvent;
import car_rental_app.domain.saga.event.ReservationCancelledEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;

import java.util.concurrent.CompletableFuture;

public interface SagaEventPublisher {
    CompletableFuture<Boolean> publishReservationCreated(ReservationCreatedEvent event);

    CompletableFuture<Boolean> publishReservationCancelled(ReservationCancelledEvent event);

    CompletableFuture<Boolean> publishPaymentFailed(PaymentFailedEvent event);
}
