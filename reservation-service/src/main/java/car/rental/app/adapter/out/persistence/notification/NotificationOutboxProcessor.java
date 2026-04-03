package car.rental.app.adapter.out.persistence.notification;

import car.rental.app.adapter.out.rest.notification.NotificationClient;
import car.rental.app.adapter.out.rest.notification.dto.ReservationNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class NotificationOutboxProcessor {

    private static final Logger log = LoggerFactory.getLogger(NotificationOutboxProcessor.class);

    private final NotificationOutboxRepository outbox;
    private final NotificationDeadLetterRepository deadLetterRepo;
    private final NotificationClient notificationClient;
    private final NotificationOutboxMetrics metrics;

    private static final int MAX_RETRIES = 10;

    public NotificationOutboxProcessor(
            NotificationOutboxRepository outbox,
            NotificationDeadLetterRepository deadLetterRepo,
            NotificationClient notificationClient,
            NotificationOutboxMetrics metrics
    ) {
        this.outbox = outbox;
        this.deadLetterRepo = deadLetterRepo;
        this.notificationClient = notificationClient;
        this.metrics = metrics;
    }

    @Scheduled(fixedDelay = 10_000)
    public void processOutbox() {

        metrics.outboxSize.set(outbox.count());

        var entries = outbox.findAll();

        for (NotificationOutboxEntry entry : entries) {

            boolean shouldSkip =
                    entry.retryCount() >= MAX_RETRIES ||
                            entry.nextAttemptAt().isAfter(Instant.now());

            if (shouldSkip) {
                if (entry.retryCount() >= MAX_RETRIES) {
                    moveToDeadLetter(entry, "Max retries exceeded");
                }
                continue; // jedyne continue w pętli
            }

            try {
                ReservationNotificationRequest req = new ReservationNotificationRequest(
                        entry.reservationId(),
                        entry.customerId(),
                        entry.email(),
                        entry.timestamp(),
                        entry.reason()
                );

                if ("RESERVATION_CONFIRMED".equals(entry.type())) {
                    notificationClient.sendReservationConfirmed(req);
                } else {
                    notificationClient.sendReservationCancelled(req);
                }

                metrics.successCounter.inc();
                outbox.delete(entry);

            } catch (Exception e) {

                metrics.retryCounter.inc();

                Instant nextAttempt = calculateBackoff(entry.retryCount() + 1);

                NotificationOutboxEntry updated = new NotificationOutboxEntry(
                        entry.id(),
                        entry.reservationId(),
                        entry.customerId(),
                        entry.email(),
                        entry.reason(),
                        entry.type(),
                        entry.timestamp(),
                        entry.retryCount() + 1,
                        e.getMessage(),
                        nextAttempt
                );

                outbox.save(updated);

                log.warn("Outbox retry FAILED for reservation {} (attempt {})",
                        entry.reservationId(), updated.retryCount());
            }
        }
    }

    private Instant calculateBackoff(int retryCount) {
        long seconds = (long) Math.min(Math.pow(2, retryCount) * 10, 1800);
        return Instant.now().plusSeconds(seconds);
    }

    private void moveToDeadLetter(NotificationOutboxEntry entry, String reason) {
        metrics.deadLetterCounter.inc();

        NotificationDeadLetterEntry dead = new NotificationDeadLetterEntry(
                entry.id(),
                entry.reservationId(),
                entry.customerId(),
                entry.email(),
                entry.reason(),
                entry.type(),
                entry.timestamp(),
                entry.retryCount(),
                entry.lastError(),
                Instant.now()
        );

        deadLetterRepo.save(dead);
        outbox.delete(entry);

        log.error("Moved notification to dead letter: reservation {} reason={}",
                entry.reservationId(), reason);
    }
}
