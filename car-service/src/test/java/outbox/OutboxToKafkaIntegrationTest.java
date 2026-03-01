package outbox;

import car_rental_app.BaseIntegrationTest;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventDocument;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.apache.kafka.clients.consumer.ConsumerRecord;


import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OutboxToKafkaIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OutboxEventRepository repository;

    @Test
    public void shouldPublishPendingEventsToKafka() {
        OutboxEventDocument doc = new OutboxEventDocument("r1",
                "ReservationCreatedEvent",
                "{\"reservationId\":\"r1\"}",
                "PENDING", Instant.now());

        repository.save(doc); // consumer
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", String.valueOf(kafka));
        var consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(List.of("reservation-events"));
                Awaitility.await().atMost(Duration.ofSeconds(10))
                        .pollInterval(Duration.ofMillis(300))
                        .untilAsserted(() -> {
                            ConsumerRecord<String, String> record = KafkaTestUtils
                                    .getSingleRecord(consumer, "saga-events");
                            assertThat(record.value()).contains("reservationId");
                        });
    }
}