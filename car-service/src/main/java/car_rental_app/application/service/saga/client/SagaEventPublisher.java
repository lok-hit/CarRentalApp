package car_rental_app.application.service.saga.client;

import car_rental_app.application.service.saga.event.PaymentFailedEvent;
import car_rental_app.application.service.saga.event.ReservationCancelledEvent;
import car_rental_app.application.service.saga.event.ReservationCreatedEvent;

public interface SagaEventPublisher {

    void publishReservationCreated(ReservationCreatedEvent event);

    void publishReservationCancelled(ReservationCancelledEvent event);

    void publishPaymentFailed(PaymentFailedEvent event);
}
