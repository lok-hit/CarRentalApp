package car.rental.app.domain.port;

import car.rental.app.domain.saga.event.PaymentFailedEvent;
import car.rental.app.domain.saga.event.ReservationCancelledEvent;
import car.rental.app.domain.saga.event.ReservationCreatedEvent;


public interface ReservationEventsPort {
    /**
 * Processes a reservation-created event.
 *
 * @param event the ReservationCreatedEvent containing the reservation details to process
 */
void onReservationCreated(ReservationCreatedEvent event);

    void onReservationCancelled(ReservationCancelledEvent event);

    void onPaymentFailed(PaymentFailedEvent event);
}
