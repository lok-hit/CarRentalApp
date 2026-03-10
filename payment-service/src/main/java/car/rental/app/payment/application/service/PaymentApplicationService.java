package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.event.PaymentCompleted;
import car.rental.app.payment.domain.event.PaymentFailed;
import car.rental.app.payment.domain.model.Money;
import car.rental.app.payment.domain.model.Payment;
import car.rental.app.payment.domain.model.PaymentStatus;
import car.rental.app.payment.domain.port.in.PaymentUseCase;
import car.rental.app.payment.domain.port.out.PaymentEventPublisher;
import car.rental.app.payment.domain.port.out.PaymentProvider;
import car.rental.app.payment.domain.port.out.PaymentProviderResult;
import car.rental.app.payment.domain.port.out.PaymentRepository;
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
            IdempotencyKeyGenerator keyGenerator, EventFactory eventFactory, PaymentAuditService auditService
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

        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                reservationId,
                customerId,
                amount,
                PaymentStatus.PENDING,
                null,
                Instant.now(),
                Instant.now()
        );

        try {

            PaymentProviderResult result = paymentProvider.charge(payment);
            auditService.audit(
                    payment,
                    "stripe",
                    requestPayload,
                    responsePayload,
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

            Payment completed = markAsSuccess(payment);

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

    private Payment markAsSuccess(Payment payment) {
        return new Payment(
                payment.id(),
                payment.reservationId(),
                payment.customerId(),
                payment.amount(),
                PaymentStatus.SUCCESS,
                Instant.now(),
                payment.createdAt(),
                Instant.now()
        );
    }
}