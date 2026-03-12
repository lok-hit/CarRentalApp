package car.rental.app.domain;

import car.rental.app.CarServiceMain;
import car.rental.app.BaseIntegrationTest;
import car.rental.app.adapter.out.messaging.outbox.OutboxEventDocument;
import car.rental.app.adapter.out.messaging.outbox.OutboxEventRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CarServiceMain.class)
class OutboxRetryMechanismIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OutboxEventRepository outbox;

    @Test
    void shouldRetryOutboxEventWhenKafkaIsBackOnline()  {

        kafka.stop();
        OutboxEventDocument event = new OutboxEventDocument(UUID.randomUUID().toString(),
                "ReservationConfirmedEvent",
                "{\"reservationId\":\"r1\",\"carId\":\"c1\",\"userId\":\"u1\"}",
                "PENDING");

        outbox.save(event);
        Awaitility.  await().atMost(2, TimeUnit.SECONDS);
        List<OutboxEventDocument> pending = outbox.findByStatus("PENDING");
        assertThat(pending).isNotEmpty();

        kafka.start();
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("retryGroup", "true", String.valueOf(kafka));
        var consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(List.of("reservation-events"));

        Awaitility.await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            ConsumerRecord<String, String> recorded = KafkaTestUtils.getSingleRecord(consumer,
                    "reservation-events");

            assertThat(recorded.value()).contains("ReservationConfirmedEvent");
        });

        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            List<OutboxEventDocument> sent = outbox.findByStatus("SENT");
            assertThat(sent).isNotEmpty();
        });
    }

}
