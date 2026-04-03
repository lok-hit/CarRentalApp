package car.rental.app.adapter.out.persistence.notification;

import org.springframework.stereotype.Component;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

@Component
public class NotificationOutboxMetrics {

    public final Counter retryCounter = Counter.build()
            .name("notification_outbox_retry_total")
            .help("Total retries attempted for notification outbox")
            .register();

    public final Counter successCounter = Counter.build()
            .name("notification_outbox_success_total")
            .help("Total successful retries")
            .register();

    public final Counter deadLetterCounter = Counter.build()
            .name("notification_outbox_dead_letter_total")
            .help("Total notifications moved to dead letter")
            .register();

    public final Gauge outboxSize = Gauge.build()
            .name("notification_outbox_size")
            .help("Current size of notification outbox")
            .register();
}
