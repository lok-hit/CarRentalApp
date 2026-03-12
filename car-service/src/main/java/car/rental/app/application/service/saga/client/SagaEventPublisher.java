package car.rental.app.application.service.saga.client;



import car.rental.app.domain.saga.event.PaymentFailedEvent;
import car.rental.app.domain.saga.event.ReservationCancelledEvent;
import car.rental.app.domain.saga.event.ReservationCreatedEvent;

import java.util.concurrent.CompletableFuture;

public interface SagaEventPublisher {
    /**
 * Publish a ReservationCreatedEvent to the saga event stream.
 *
 * @param event the reservation created event to publish
 * @return `true` if the event was published successfully, `false` otherwise
 */
CompletableFuture<Boolean> publishReservationCreated(ReservationCreatedEvent event);

    /**
 * Publishes a reservation cancellation event to saga participants.
 *
 * @param event the reservation cancellation event to publish
 * @return `true` if the event was published successfully, `false` otherwise
 */
CompletableFuture<Boolean> publishReservationCancelled(ReservationCancelledEvent event);

    /**
 * Publishes a payment-failed event to saga participants.
 *
 * @param event the PaymentFailedEvent containing reservation and failure details
 * @return `true` if the event was accepted for delivery, `false` otherwise
 */
CompletableFuture<Boolean> publishPaymentFailed(PaymentFailedEvent event);
}
