package car.rental.app.adapter.out.event;

import car.rental.app.application.port.DomainEventPublisher;
import car.rental.app.domain.event.DomainEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DomainEventKafkaRelay {

    private final DomainEventPublisher kafkaPublisher;

    public DomainEventKafkaRelay(DomainEventPublisher kafkaPublisher) {
        this.kafkaPublisher = kafkaPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDomainEvent(DomainEvent event) {
        kafkaPublisher.publish(event);
    }
}

