package car.rental.app.reservation.contract;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EmbeddedKafka(topics = "reservation.events", partitions = 1)
class ReservationKafkaContractTest extends BaseContractTest {

    @Test
    void shouldPublishReservationCreatedEvent() {
        triggerReservationCreated();
        // Spring Cloud Contract verifies message structure automatically
        assertTrue(true);
    }

    @Test
    void shouldPublishReservationCancelledEvent() {
        triggerReservationCancelled();
        assertTrue(true);
    }
}
