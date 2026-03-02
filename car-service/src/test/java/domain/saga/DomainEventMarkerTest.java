package domain.saga;

import car_rental_app.domain.event.DomainEvent;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.saga.event.PaymentFailedEvent;
import car_rental_app.domain.saga.event.ReservationCancelledEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainEventMarkerTest {
    @Test
    void reservationCreatedEventShouldImplementDomainEvent() {
        var event = new ReservationCreatedEvent("r1", new CarId("c1"), "u1");
        assertThat(event).isInstanceOf(DomainEvent.class);
    }

    @Test
    void reservationCancelledEventShouldImplementDomainEvent() {
        var event = new ReservationCancelledEvent("r1", new CarId("c2"), "u2", Instant.now());
        assertThat(event).isInstanceOf(DomainEvent.class);
    }

    @Test
    void paymentFailedEventShouldImplementDomainEvent() {
        var event = new PaymentFailedEvent("r1", new CarId("c1"), "u3");
        assertThat(event).isInstanceOf(DomainEvent.class);
    }
}
