package car.rental.app.payment.domain.model;


import java.time.Instant;

public record Payment(
        String id,
        String reservationId,
        String customerId,
        Money amount,
        PaymentStatus status,
        String providerPaymentId,
        Instant paidAt,
        Instant createdAt,
        Instant updatedAt
) {

    public Payment withStatus(PaymentStatus newStatus) {
        return new Payment(
                this.id,
                this.reservationId,
                this.customerId,
                this.amount,
                newStatus,
                this.providerPaymentId,
                this.paidAt,
                this.createdAt,
                Instant.now()
        );
    }

    public Payment withProviderPaymentId(String providerPaymentId) {
        return new Payment(
                this.id,
                this.reservationId,
                this.customerId,
                this.amount,
                this.status,
                providerPaymentId,
                this.paidAt,
                this.createdAt,
                Instant.now()
        );
    }

    public Payment withPaidAt(Instant paidAt) {
        return new Payment(
                this.id,
                this.reservationId,
                this.customerId,
                this.amount,
                this.status,
                this.providerPaymentId,
                paidAt,
                this.createdAt,
                Instant.now()
        );
    }
}


