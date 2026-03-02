package domain.port;

import car_rental_app.domain.model.CarId;
import car_rental_app.domain.event.CarCreatedEvent;
import car_rental_app.domain.port.OutboxEventStore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboxEventStoreTest {

    @Test
    void saveEventShouldBeImplementedByAdapter() {
        OutboxEventStore store = (id, event) -> {
            throw new UnsupportedOperationException("not implemented");
        };

        assertThatThrownBy(() ->
                store.saveEvent("c1", new CarCreatedEvent("c1", "SEDAN", BigDecimal.valueOf(1000)))
        ).isInstanceOf(UnsupportedOperationException.class);
    }
}
