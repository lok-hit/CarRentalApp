package car.rental.app.payment.adapter.out.audit;

import car.rental.app.payment.domain.audit.PaymentAuditEntry;
import car.rental.app.payment.domain.port.out.PaymentAuditRepository;

public class PaymentAuditRepositoryAdapter implements PaymentAuditRepository {
    private final SpringDataPaymentAuditRepository repository;

    public PaymentAuditRepositoryAdapter(SpringDataPaymentAuditRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(PaymentAuditEntry entry) {

        PaymentAuditDocument doc = PaymentAuditDocument.builder()
                .paymentId(entry.paymentId())
                .reservationId(entry.reservationId())
                .customerId(entry.customerId())
                .provider(entry.provider())
                .requestPayload(entry.requestPayload())
                .responsePayload(entry.responsePayload())
                .success(entry.success())
                .failureReason(entry.failureReason())
                .occurredAt(entry.occurredAt())
                .traceId(entry.traceId())
                .correlationId(entry.correlationId())
                .build();

        repository.save(doc);
    }
}

