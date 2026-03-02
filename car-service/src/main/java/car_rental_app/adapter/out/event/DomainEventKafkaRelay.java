package car_rental_app.adapter.out.event;

import car_rental_app.application.port.DomainEventPublisher;
import car_rental_app.domain.event.DomainEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;

@Component
public class DomainEventKafkaRelay {

    private final car_rental_app.application.port.DomainEventPublisher kafkaPublisher;

    public DomainEventKafkaRelay(DomainEventPublisher kafkaPublisher) {
        this.kafkaPublisher = kafkaPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDomainEvent(DomainEvent event) {
        kafkaPublisher.publish(event);
    }
}

