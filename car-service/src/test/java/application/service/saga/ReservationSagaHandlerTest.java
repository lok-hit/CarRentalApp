package application.service.saga;

import car_rental_app.application.command.MarkCarAsAvailableCommand;
import car_rental_app.application.command.MarkCarAsUnavailableCommand;
import car_rental_app.application.service.saga.ReservationSagaHandler;
import car_rental_app.domain.model.CarId;
import car_rental_app.application.port.CarCommandPort;
import car_rental_app.domain.port.OutboxEventStore;
import car_rental_app.domain.saga.event.PaymentFailedEvent;
import car_rental_app.domain.saga.event.ReservationCancelledEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReservationSagaHandlerTest {
    private CarCommandPort carCommandPort;
    private OutboxEventStore outbox;
    private ReservationSagaHandler handler;

    @BeforeEach
    void setup() {
        carCommandPort = mock(CarCommandPort.class);
        outbox = mock(OutboxEventStore.class);
        handler = new ReservationSagaHandler(carCommandPort, outbox);
    }

    @Test
    void onReservationCreatedShouldMarkCarUnavailable() {
        var event = new ReservationCreatedEvent("r1", new CarId("c1"), "u8");
        handler.onReservationCreated(event);
        verify(carCommandPort).handle(new MarkCarAsUnavailableCommand(new CarId("c1").value()));
    }

    @Test
    void onReservationCancelledShouldMarkCarAvailable() {
        var event = new ReservationCancelledEvent("r1", new CarId("c1"), "u7", java.time.Instant.now());
        handler.onReservationCancelled(event);
        verify(carCommandPort).handle(new MarkCarAsAvailableCommand(new CarId("c1")));
    }

    @Test
    void onReservationFailedShouldMarkCarAvailable() {
        var event = new PaymentFailedEvent("r1", new CarId("c1"), "u4");
        handler.onReservationFailed(event);
        verify(carCommandPort).handle(new MarkCarAsAvailableCommand(new CarId("c1")));
    }
}
