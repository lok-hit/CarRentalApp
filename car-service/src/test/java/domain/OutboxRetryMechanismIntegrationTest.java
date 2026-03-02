package domain;

import car_rental_app.BaseIntegrationTest;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventDocument;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = car_rental_app.CarServiceMain.class)
public class OutboxRetryMechanismIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OutboxEventRepository outbox;

    @Test
    public void shouldRetryOutboxEventWhenKafkaIsBackOnline() throws Exception {

        kafka.stop();
        OutboxEventDocument event = new OutboxEventDocument(UUID.randomUUID().toString(),
                "ReservationConfirmedEvent",
                "{\"reservationId\":\"r1\",\"carId\":\"c1\",\"userId\":\"u1\"}",
                "PENDING", Instant.now());

        outbox.save(event);
        Thread.sleep(3000);
        List<OutboxEventDocument> pending = outbox.findByStatus("PENDING");
        assertThat(pending).isNotEmpty();

        kafka.start();
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("retryGroup", "true", String.valueOf(kafka));
        var consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(List.of("reservation-events"));

        Awaitility.await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer,
                    "reservation-events");

            assertThat(record.value()).contains("ReservationConfirmedEvent");
        });

        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            List<OutboxEventDocument> sent = outbox.findByStatus("SENT");
            assertThat(sent).isNotEmpty();
        });
    }

}
