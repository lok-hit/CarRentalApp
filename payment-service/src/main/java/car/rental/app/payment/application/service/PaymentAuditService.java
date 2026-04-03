package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.audit.PaymentAuditEntry;
import car.rental.app.payment.domain.model.Payment;
import car.rental.app.payment.domain.port.out.PaymentAuditRepository;
import car.rental.app.payment.domain.port.out.PaymentProviderResult;
import car.rental.app.payment.domain.port.out.RefundProviderResult;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentAuditService {

    private final PaymentAuditRepository repository;
    private final CorrelationIdService correlationIdService;

    public PaymentAuditService(PaymentAuditRepository repository, CorrelationIdService correlationIdService) {
        this.repository = repository;
        this.correlationIdService = correlationIdService;
    }

    public void audit(Payment payment, String provider, String request, String response, PaymentProviderResult result) {
        PaymentAuditEntry entry = new PaymentAuditEntry(payment.id(), payment.reservationId(), payment.customerId(), provider, request, response, result.success(), result.failureReason(), Instant.now(), correlationIdService.getOrCreateTraceId(), correlationIdService.getOrCreateCorrelationId());

        repository.save(entry);
    }

    public void auditRefund(
            Payment payment,
            String provider,
            String request,
            String response,
            RefundProviderResult result
    ) {
        PaymentAuditEntry entry = new PaymentAuditEntry(
                payment.id(),
                payment.reservationId(),
                payment.customerId(),
                provider,
                request,
                response,
                result.success(),
                result.failureReason(),
                Instant.now(),
                correlationIdService.getOrCreateTraceId(),
                correlationIdService.getOrCreateCorrelationId()
        );

        repository.save(entry);
    }

    public void recordReceived(Object command) {
        PaymentAuditEntry entry = new PaymentAuditEntry(
                null,
                null,
                null,
                "saga",
                command.toString(),
                null,
                true,
                null,
                Instant.now(),
                correlationIdService.getOrCreateTraceId(),
                correlationIdService.getOrCreateCorrelationId()
        );
        repository.save(entry);
    }

    public void recordSuccess(Payment payment) {
        PaymentAuditEntry entry = new PaymentAuditEntry(
                payment.id(),
                payment.reservationId(),
                payment.customerId(),
                "saga",
                "payment_completed",
                null,
                true,
                null,
                Instant.now(),
                correlationIdService.getOrCreateTraceId(),
                correlationIdService.getOrCreateCorrelationId()
        );
        repository.save(entry);
    }

    public void recordRefund(Payment payment) {
        PaymentAuditEntry entry = new PaymentAuditEntry(
                payment.id(),
                payment.reservationId(),
                payment.customerId(),
                "saga",
                "payment_refunded",
                null,
                true,
                null,
                Instant.now(),
                correlationIdService.getOrCreateTraceId(),
                correlationIdService.getOrCreateCorrelationId()
        );
        repository.save(entry);
    }

    public void recordFailure(Object command, Exception ex) {
        PaymentAuditEntry entry = new PaymentAuditEntry(
                null,
                null,
                null,
                "saga",
                command.toString(),
                null,
                false,
                ex.getMessage(),
                Instant.now(),
                correlationIdService.getOrCreateTraceId(),
                correlationIdService.getOrCreateCorrelationId()
        );
        repository.save(entry);
    }

    public void recordFailure(Payment payment, Exception ex) {
        PaymentAuditEntry entry = new PaymentAuditEntry(
                payment.id(),
                payment.reservationId(),
                payment.customerId(),
                "saga",
                "payment_failed",
                null,
                false,
                ex.getMessage(),
                Instant.now(),
                correlationIdService.getOrCreateTraceId(),
                correlationIdService.getOrCreateCorrelationId()
        );
        repository.save(entry);
    }
}
