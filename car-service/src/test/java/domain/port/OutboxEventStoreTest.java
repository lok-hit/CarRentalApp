package car_rental_app.domain.port;

import car_rental_app.domain.model.CarId;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboxEventStoreTest {

    @Test
    void saveEventShouldBeImplementedByAdapter() {
        OutboxEventStore store = (id, event) -> {
            throw new UnsupportedOperationException("not implemented");
        };

        assertThatThrownBy(() ->
                store.saveEvent("r1", new ReservationCreatedEvent("r1", new CarId("c1")))
        ).isInstanceOf(UnsupportedOperationException.class);
    }
}
