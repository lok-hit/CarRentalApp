package saga;

import car.rental.app.CarServiceMain;
import car.rental.app.BaseIntegrationTest;
import car.rental.app.adapter.out.messaging.outbox.OutboxEventRepository;
import car.rental.app.domain.model.CarId;
import car.rental.app.domain.saga.event.ReservationCancelledEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CarServiceMain.class)
class ReservationCancelledIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OutboxEventRepository outbox;


    @Test
    void shouldHandleReservationCancelledEventAndPublishToKafka() throws Exception {
        // given
        var producer = new KafkaProducer<String, String>(
                Map.of("bootstrap.servers", kafka.getBootstrapServers(),
                        "key.serializer",
                        "org.apache.kafka.common.serialization.StringSerializer",
                        "value.serializer",
                        "org.apache.kafka.common.serialization.StringSerializer")
        );

        ReservationCancelledEvent event = new ReservationCancelledEvent("r1", new CarId("c1"), "9", Instant.now());
        String json = new ObjectMapper().writeValueAsString(event);
        // when
        producer.send(new ProducerRecord<>("reservation-cancelled", json));

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    var pending = outbox.findByStatus("PENDING");

                    assertThat(pending).isNotEmpty();
                    assertThat(pending.get(0).getEventType()).isEqualTo("ReservationCancelledEvent");
                });

        // then
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("cancelGroup", "true", String.valueOf(kafka));
        var consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(List.of("reservation-events"));
        Awaitility.await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    ConsumerRecord<String, String> recorded = KafkaTestUtils
                            .getSingleRecord(consumer, "reservation-events");

                    assertThat(recorded.value()).contains("ReservationCancelledEvent");
                });
    }
}
