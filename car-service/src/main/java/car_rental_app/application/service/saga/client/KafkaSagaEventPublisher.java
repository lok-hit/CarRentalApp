package car_rental_app.application.service.saga.client;


import car_rental_app.domain.saga.event.PaymentFailedEvent;
import car_rental_app.domain.saga.event.ReservationCancelledEvent;
import car_rental_app.domain.saga.event.ReservationCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSagaEventPublisher implements SagaEventPublisher {

    private final KafkaTemplate<String, Object> kafka;

    public KafkaSagaEventPublisher(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    @Override
    public void publishReservationCreated(ReservationCreatedEvent event) {

        kafka.send("reservation-created", event.carId().value(), event);
    }

    @Override
    public void publishReservationCancelled(ReservationCancelledEvent event) {

        kafka.send("reservation-cancelled", event.carId().value(), event);
    }

    @Override
    public void publishPaymentFailed(PaymentFailedEvent event) {

        kafka.send("payment-failed", event.carId().value(), event);

    }
}
