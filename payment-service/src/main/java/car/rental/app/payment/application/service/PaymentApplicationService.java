package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.event.PaymentCompleted;
import car.rental.app.payment.domain.event.PaymentFailed;
import car.rental.app.payment.domain.model.Money;
import car.rental.app.payment.domain.model.Payment;
import car.rental.app.payment.domain.model.PaymentStatus;
import car.rental.app.payment.domain.port.in.PaymentUseCase;
import car.rental.app.payment.domain.port.out.PaymentEventPublisher;
import car.rental.app.payment.domain.port.out.PaymentProvider;
import car.rental.app.payment.domain.port.out.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentApplicationService implements PaymentUseCase {

    private final PaymentRepository repository;
    private final PaymentEventPublisher eventPublisher;
    private final PaymentProvider paymentProvider;

    public PaymentApplicationService(
            PaymentRepository repository,
            PaymentEventPublisher eventPublisher, PaymentProvider paymentProvider
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.paymentProvider = paymentProvider;
    }

    @Override
    public Payment processPayment(String reservationId, String customerId, Money amount) {

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
            boolean charged = paymentProvider.charge(payment);

            if (!charged) {
                PaymentFailed failed = new PaymentFailed(
                        payment.id(),
                        payment.reservationId(),
                        "Stripe declined the payment"
                );
                eventPublisher.publish(failed);
                return payment;
            }

            Payment completed = markAsSuccess(payment);

            repository.save(completed);
            eventPublisher.publish(new PaymentCompleted(completed));

            return completed;

        } catch (Exception ex) {

            PaymentFailed failed = new PaymentFailed(
                    payment.id(),
                    payment.reservationId(),
                    ex.getMessage()
            );

            eventPublisher.publish(failed);

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
