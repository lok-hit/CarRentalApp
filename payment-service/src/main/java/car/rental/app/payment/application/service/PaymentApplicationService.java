package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.event.PaymentCompleted;
import car.rental.app.payment.domain.event.PaymentFailed;
import car.rental.app.payment.domain.model.Money;
import car.rental.app.payment.domain.model.Payment;
import car.rental.app.payment.domain.model.PaymentStatus;
import car.rental.app.payment.domain.port.in.PaymentUseCase;
import car.rental.app.payment.domain.port.out.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentApplicationService implements PaymentUseCase {

    private final PaymentRepository repository;
    private final PaymentEventPublisher eventPublisher;
    private final PaymentProvider paymentProvider;
    private final IdempotencyService idempotencyService;
    private final IdempotencyKeyGenerator keyGenerator;
    private final EventFactory eventFactory;
    private final PaymentAuditService auditService;

    public PaymentApplicationService(
            PaymentRepository repository,
            PaymentEventPublisher eventPublisher,
            PaymentProvider paymentProvider,
            IdempotencyService idempotencyService,
            IdempotencyKeyGenerator keyGenerator,
            EventFactory eventFactory,
            PaymentAuditService auditService
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.paymentProvider = paymentProvider;
        this.idempotencyService = idempotencyService;
        this.keyGenerator = keyGenerator;
        this.eventFactory = eventFactory;
        this.auditService = auditService;
    }

    @Override
    public Payment processPayment(String reservationId, String customerId, Money amount) {

        String key = keyGenerator.generate(reservationId, customerId, amount);

        if (idempotencyService.isDuplicate(key)) {
            return repository.findByReservationId(reservationId).stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Idempotent payment not found"));
        }

        Instant now = Instant.now();

        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                reservationId,
                customerId,
                amount,
                PaymentStatus.PENDING,
                null,      // providerPaymentId
                null,      // paidAt
                now,       // createdAt
                now        // updatedAt
        );

        repository.save(payment);

        try {
            PaymentProviderResult result = paymentProvider.charge(payment);

            auditService.audit(
                    payment,
                    "stripe",
                    result.requestPayload(),
                    result.responsePayload(),
                    result
            );

            if (!result.success()) {
                eventPublisher.publish(eventFactory.paymentFailed(
                        payment.id(),
                        payment.reservationId(),
                        result.failureReason()
                ));
                return payment;
            }

            Payment completed = new Payment(
                    payment.id(),
                    payment.reservationId(),
                    payment.customerId(),
                    payment.amount(),
                    PaymentStatus.SUCCESS,
                    result.providerPaymentId(),
                    Instant.now(),      // paidAt
                    payment.createdAt(),
                    Instant.now()       // updatedAt
            );

            repository.save(completed);
            idempotencyService.markProcessed(key);
            eventPublisher.publish(eventFactory.paymentCompleted(completed));

            return completed;

        } catch (Exception ex) {

            eventPublisher.publish(eventFactory.paymentFailed(
                    payment.id(),
                    payment.reservationId(),
                    ex.getMessage()
            ));

            return payment;
        }
    }

    public Payment refundPayment(String paymentId) {

        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new IllegalStateException("Payment not found"));

        RefundProviderResult result = paymentProvider.refund(payment);

        auditService.auditRefund(
                payment,
                "stripe",
                result.requestPayload(),
                result.responsePayload(),
                result
        );

        if (!result.success()) {
            eventPublisher.publish(eventFactory.refundFailed(
                    payment.id(),
                    payment.reservationId(),
                    result.failureReason()
            ));
            return payment;
        }

        Payment refunded = new Payment(
                payment.id(),
                payment.reservationId(),
                payment.customerId(),
                payment.amount(),
                PaymentStatus.REFUNDED,
                payment.providerPaymentId(),
                payment.paidAt(),
                payment.createdAt(),
                Instant.now()
        );

        repository.save(refunded);

        eventPublisher.publish(eventFactory.refundCompleted(refunded));

        return refunded;
    }
}
