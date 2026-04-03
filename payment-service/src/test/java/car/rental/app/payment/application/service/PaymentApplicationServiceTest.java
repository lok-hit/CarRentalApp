package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.event.PaymentCompleted;
import car.rental.app.payment.domain.event.PaymentFailed;
import car.rental.app.payment.domain.event.RefundCompleted;
import car.rental.app.payment.domain.event.RefundFailed;
import car.rental.app.payment.domain.model.Money;
import car.rental.app.payment.domain.model.Payment;
import car.rental.app.payment.domain.model.PaymentStatus;
import car.rental.app.payment.domain.port.out.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentApplicationServiceTest {

    @Mock private PaymentRepository repository;
    @Mock private PaymentEventPublisher eventPublisher;
    @Mock private PaymentProvider paymentProvider;
    @Mock private IdempotencyService idempotencyService;
    @Mock private IdempotencyKeyGenerator keyGenerator;
    @Mock private EventFactory eventFactory;
    @Mock private PaymentAuditService auditService;

    private PaymentApplicationService service;

    private static final Money AMOUNT = new Money(new BigDecimal("150.00"), Currency.getInstance("PLN"));

    @BeforeEach
    void setUp() {
        service = new PaymentApplicationService(
                repository, eventPublisher, paymentProvider,
                idempotencyService, keyGenerator, eventFactory, auditService
        );
    }

    @Test
    void processPayment_success_savesCompletedAndPublishesEvent() {
        when(keyGenerator.generate(anyString(), anyString(), any())).thenReturn("key-1");
        when(idempotencyService.isDuplicate("key-1")).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(paymentProvider.charge(any())).thenReturn(
                PaymentProviderResult.ok("stripe-pi-1", "{}", "{}"));

        PaymentCompleted completedEvent = new PaymentCompleted(
                "pay-id", "res-1", "cust-1", AMOUNT, "stripe-pi-1", Instant.now(), null);
        when(eventFactory.paymentCompleted(any())).thenReturn(completedEvent);

        Payment result = service.processPayment("res-1", "cust-1", AMOUNT);

        assertThat(result.status()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(result.providerPaymentId()).isEqualTo("stripe-pi-1");
        verify(idempotencyService).markProcessed("key-1");
        verify(eventPublisher).publish(completedEvent);
        verify(repository, times(2)).save(any()); // pending + success
    }

    @Test
    void processPayment_providerFails_publishesPaymentFailed() {
        when(keyGenerator.generate(anyString(), anyString(), any())).thenReturn("key-2");
        when(idempotencyService.isDuplicate("key-2")).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(paymentProvider.charge(any())).thenReturn(
                PaymentProviderResult.failed("card declined", "{}", "{}"));

        PaymentFailed failedEvent = new PaymentFailed("pay-id", "res-2", "card declined", null);
        when(eventFactory.paymentFailed(anyString(), anyString(), anyString())).thenReturn(failedEvent);

        Payment result = service.processPayment("res-2", "cust-2", AMOUNT);

        assertThat(result.status()).isEqualTo(PaymentStatus.PENDING);
        verify(eventPublisher).publish(failedEvent);
        verify(idempotencyService, never()).markProcessed(any());
    }

    @Test
    void processPayment_isDuplicate_returnsExistingPayment() {
        when(keyGenerator.generate(anyString(), anyString(), any())).thenReturn("key-dup");
        when(idempotencyService.isDuplicate("key-dup")).thenReturn(true);

        Payment existing = new Payment("pay-old", "res-dup", "cust-1", AMOUNT,
                PaymentStatus.SUCCESS, "stripe-pi-old", Instant.now(), Instant.now(), Instant.now());
        when(repository.findByReservationId("res-dup")).thenReturn(List.of(existing));

        Payment result = service.processPayment("res-dup", "cust-1", AMOUNT);

        assertThat(result.id()).isEqualTo("pay-old");
        verifyNoInteractions(paymentProvider);
    }

    @Test
    void processPayment_providerThrowsException_publishesPaymentFailed() {
        when(keyGenerator.generate(anyString(), anyString(), any())).thenReturn("key-ex");
        when(idempotencyService.isDuplicate("key-ex")).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(paymentProvider.charge(any())).thenThrow(new RuntimeException("Stripe unavailable"));

        PaymentFailed failedEvent = new PaymentFailed("pay-id", "res-3", "Stripe unavailable", null);
        when(eventFactory.paymentFailed(anyString(), anyString(), anyString())).thenReturn(failedEvent);

        Payment result = service.processPayment("res-3", "cust-3", AMOUNT);

        assertThat(result.status()).isEqualTo(PaymentStatus.PENDING);
        verify(eventPublisher).publish(failedEvent);
    }

    @Test
    void refundPayment_success_savesRefundedAndPublishesEvent() {
        Payment existingPayment = new Payment("pay-1", "res-1", "cust-1", AMOUNT,
                PaymentStatus.SUCCESS, "stripe-pi-1", Instant.now(), Instant.now(), Instant.now());
        when(repository.findById("pay-1")).thenReturn(Optional.of(existingPayment));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(paymentProvider.refund(any())).thenReturn(
                RefundProviderResult.ok("{}", "{}"));

        RefundCompleted refundEvent = new RefundCompleted(
                "pay-1", "res-1", "cust-1", AMOUNT, "stripe-pi-1", Instant.now(), null);
        when(eventFactory.refundCompleted(any())).thenReturn(refundEvent);

        Payment result = service.refundPayment("pay-1");

        assertThat(result.status()).isEqualTo(PaymentStatus.REFUNDED);
        verify(eventPublisher).publish(refundEvent);
    }

    @Test
    void refundPayment_providerFails_publishesRefundFailed() {
        Payment existingPayment = new Payment("pay-2", "res-2", "cust-2", AMOUNT,
                PaymentStatus.SUCCESS, "stripe-pi-2", Instant.now(), Instant.now(), Instant.now());
        when(repository.findById("pay-2")).thenReturn(Optional.of(existingPayment));
        when(paymentProvider.refund(any())).thenReturn(
                RefundProviderResult.failed("insufficient funds", "{}", "{}"));

        RefundFailed refundFailedEvent = new RefundFailed("pay-2", "res-2", "insufficient funds", null);
        when(eventFactory.refundFailed(anyString(), anyString(), anyString())).thenReturn(refundFailedEvent);

        Payment result = service.refundPayment("pay-2");

        assertThat(result.status()).isEqualTo(PaymentStatus.SUCCESS);
        verify(eventPublisher).publish(refundFailedEvent);
    }

    @Test
    void refundPayment_paymentNotFound_throwsException() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.refundPayment("missing"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Payment not found");
    }
}
