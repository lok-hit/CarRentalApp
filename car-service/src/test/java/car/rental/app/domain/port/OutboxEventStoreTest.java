package car.rental.app.domain.port;

import car.rental.app.domain.event.CarCreatedEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboxEventStoreTest {

    @Test
    void saveEventShouldBeImplementedByAdapter() {
        OutboxEventStore store = (id, event) -> {
            throw new UnsupportedOperationException("not implemented");
        };

        CarCreatedEvent testEvent = new CarCreatedEvent("c1", "SEDAN", BigDecimal.valueOf(1000));

        assertThatThrownBy(() -> store.saveEvent("c1", testEvent))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
