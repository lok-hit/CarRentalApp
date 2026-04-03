package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.model.Payment;
import car.rental.app.payment.domain.port.out.PaymentProviderResult;
import car.rental.app.payment.domain.port.out.RefundProviderResult;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class MetricsService {

    private static final String PROVIDER = "provider";
    private final MeterRegistry registry;
    private static String CURRENCY = "currency";

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
                .tag(PROVIDER, provider)
                .tag(CURRENCY, payment.amount().currency().getCurrencyCode())
                .tag("success", String.valueOf(result.success()))
                .register(registry)
                .record(duration);

        if (result.success()) {
            registry.counter("payment.success.total",
                    PROVIDER, provider,
                    "currency", payment.amount().currency().getCurrencyCode()
            ).increment();
        } else {
            registry.counter("payment.failed.total",
                    PROVIDER, provider,
                    CURRENCY, payment.amount().currency().getCurrencyCode()
            ).increment();
        }
    }

    public void recordProviderCall(
            Payment payment,
            String provider,
            Instant start,
            RefundProviderResult result
    ) {
        Duration duration = Duration.between(start, Instant.now());

        Timer.builder("refund.provider.latency")
                .tag(PROVIDER, provider)
                .tag(CURRENCY, payment.amount().currency().getCurrencyCode())
                .tag("success", String.valueOf(result.success()))
                .register(registry)
                .record(duration);

        if (result.success()) {
            registry.counter("refund.success.total",
                    PROVIDER, provider,
                    CURRENCY, payment.amount().currency().getCurrencyCode()
            ).increment();
        } else {
            registry.counter("refund.failed.total",
                    PROVIDER, provider,
                    CURRENCY, payment.amount().currency().getCurrencyCode()
            ).increment();
        }
    }

    public void recordProviderException(Payment payment, String provider) {
        registry.counter("payment.provider.exception.total",
                PROVIDER, provider,
                CURRENCY, payment.amount().currency().getCurrencyCode()
        ).increment();
    }

    public void incrementSagaStep(String step) {
        registry.counter("saga.step.completed", "step", step).increment();
    }
}
