package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.model.Payment;
import car.rental.app.payment.domain.port.out.PaymentProviderResult;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class MetricsService {

    private final MeterRegistry registry;

    public MetricsService(MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordProviderCall(
            Payment payment,
            String provider,
            Instant start,
            PaymentProviderResult result
    ) {
        Duration duration = Duration.between(start, Instant.now());

        Timer.builder("payment.provider.latency")
                .tag("provider", provider)
                .tag("currency", payment.amount().currency().getCurrencyCode())
                .tag("success", String.valueOf(result.success()))
                .register(registry)
                .record(duration);

        if (result.success()) {
            registry.counter("payment.success.total",
                    "provider", provider,
                    "currency", payment.amount().currency().getCurrencyCode()
            ).increment();
        } else {
            registry.counter("payment.failed.total",
                    "provider", provider,
                    "currency", payment.amount().currency().getCurrencyCode()
            ).increment();
        }
    }

    public void recordProviderException(Payment payment, String provider) {
        registry.counter("payment.provider.exception.total",
                "provider", provider,
                "currency", payment.amount().currency().getCurrencyCode()
        ).increment();
    }
}
