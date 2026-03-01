package adapter.in;

import car_rental_app.adapter.in.messaging.ReservationSagaListener;
import car_rental_app.application.service.saga.ReservationSagaHandler;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.saga.event.PaymentFailedEvent;
import car_rental_app.domain.saga.event.ReservationCancelledEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ReservationSagaListenerTest {

    private ReservationSagaHandler handler;
    private ReservationSagaListener listener;

    @BeforeEach
    void setup() {
        handler = mock(ReservationSagaHandler.class);
        listener = new ReservationSagaListener(handler);
    }

    @Test
    void shouldDelegateReservationCreatedEvent() {
        var event = new ReservationCreatedEvent("r1", new CarId("c1"));

        listener.onReservationCreated(event);

        verify(handler).onReservationCreated(event);
    }

    @Test
    void shouldDelegateReservationCancelledEvent() {
        var event = new ReservationCancelledEvent("r1", new CarId("c1"));

        listener.onReservationCancelled(event);

        verify(handler).onReservationCancelled(event);
    }

    @Test
    void shouldDelegatePaymentFailedEvent() {
        var event = new PaymentFailedEvent("r1", new CarId("c1"));

        listener.onPaymentFailed(event);

        verify(handler).onReservationFailed(event);
    }
}
