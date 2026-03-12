package car.rental.app.adapter.out.messaging.outbox;

import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class OutboxEventPublisher {

    private static final Logger log = Logger.getLogger(OutboxEventPublisher.class.getName());

    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, String> kafka;

    public OutboxEventPublisher(OutboxEventRepository repository,
                                KafkaTemplate<String, String> kafka) {
        this.repository = repository;
        this.kafka = kafka;
    }

    @Scheduled(fixedDelay = 2000)
    public void publishPendingEvents() {
        List<OutboxEventDocument> pending = repository.findByStatus("PENDING");

        if (pending.isEmpty()) {
            log.fine(()-> prefix() + "[OUTBOX] No pending events to publish.");
            return;
        }

        log.info(()-> prefix() +"[OUTBOX] Found " + pending.size() + " pending events to publish.");

        for (OutboxEventDocument doc : pending) {
            try {
                log.info(()-> prefix() + "[OUTBOX] Publishing event: reservationId=" + doc.getAggregateId()
                        + ", type=" + doc.getEventType());

                kafka.send("reservation-events", doc.getPayload());

                doc.setStatus("PROCESSED");
                repository.save(doc);

                log.info(()-> prefix() +"[OUTBOX] Event published and marked as PROCESSED: id=" + doc.getId());

            } catch (Exception e) {
                log.severe("[OUTBOX] Failed to publish event id=" + doc.getId()
                        + " error=" + e.getMessage());
            }
        }
    }

    private String prefix() {
        return "[traceId=" + MDC.get("traceId") + " spanId=" + MDC.get("spanId") + "] ";
    }
}
