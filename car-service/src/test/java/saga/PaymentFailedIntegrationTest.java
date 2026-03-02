package saga;

import car_rental_app.BaseIntegrationTest;
import car_rental_app.CarServiceMain;
import car_rental_app.adapter.out.messaging.outbox.OutboxEventRepository;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.saga.event.PaymentFailedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = car_rental_app.CarServiceMain.class)
public class PaymentFailedIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OutboxEventRepository repository;

    @Test void shouldHandlePaymentFailedEventAndPublishRollbackEventToKafka() throws Exception {

        // given
        var producer = new KafkaProducer<String, String>( Map.of(
                "bootstrap.servers",
                kafka.getBootstrapServers(),
                "key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"
        ) );

        PaymentFailedEvent event = new PaymentFailedEvent("r1", new CarId("c1"), "8");
        String json = new ObjectMapper().writeValueAsString(event);

        // when
        producer.send(new ProducerRecord<>("payment-failed", json));

        // then
        Awaitility.await() .atMost(Duration.ofSeconds(10)) .untilAsserted(() -> {
            var pending = repository.findByStatus("PENDING");

            assertThat(pending).isNotEmpty();
            assertThat(pending.get(0).getEventType()).isEqualTo("ReservationRollbackEvent"); });

        // then

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("rollbackGroup", "true", String.valueOf(kafka));
        var consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps) .createConsumer();
        consumer.subscribe(List.of("reservation-events"));

        Awaitility.await() .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "reservation-events");

                    assertThat(record.value()).contains("ReservationRollbackEvent");
                    assertThat(record.value()).contains("r1");
                });
    }
}
