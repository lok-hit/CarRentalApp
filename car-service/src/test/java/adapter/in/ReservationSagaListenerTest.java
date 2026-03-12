package adapter.in;

import car.rental.app.adapter.in.messaging.ReservationSagaListener;
import car.rental.app.application.service.saga.ReservationSagaHandler;
import car.rental.app.domain.model.CarId;
import car.rental.app.domain.saga.event.PaymentFailedEvent;
import car.rental.app.domain.saga.event.ReservationCancelledEvent;
import car.rental.app.domain.saga.event.ReservationCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.mockito.Mockito.*;

class ReservationSagaListenerTest {

    private ReservationSagaHandler handler;
    private ReservationSagaListener listener;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        handler = mock(ReservationSagaHandler.class);
        listener = new ReservationSagaListener(handler);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Test
    void shouldDelegateReservationCreatedEvent() throws Exception {
        var event = new ReservationCreatedEvent("r1", new CarId("c1"), "u1");
        String json = objectMapper.writeValueAsString(event);

        listener.onReservationCreated(json);

        verify(handler).onReservationCreated(event);
    }

    @Test
    void shouldDelegateReservationCancelledEvent() throws Exception {
        var event = new ReservationCancelledEvent("r1", new CarId("c1"), "u7", Instant.now());
        String json = objectMapper.writeValueAsString(event);

        listener.onReservationCancelled(json);

        verify(handler).onReservationCancelled(event);
    }

    @Test
    void shouldDelegatePaymentFailedEvent() throws Exception {
        var event = new PaymentFailedEvent("r1", new CarId("c1"), "u4");
        String json = objectMapper.writeValueAsString(event);

        listener.onPaymentFailed(json);

        verify(handler).onReservationFailed(event);
    }
}
