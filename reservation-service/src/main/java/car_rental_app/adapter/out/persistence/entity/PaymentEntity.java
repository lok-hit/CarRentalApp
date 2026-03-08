package car_rental_app.adapter.out.persistence.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "payments")
@Data
@Builder
public class PaymentEntity {

    @Id
    private String id;

    @Indexed
    private String reservationId;

    @Indexed
    private String customerId;

    private BigDecimal amount;

    private PaymentStatus status;

    private Instant paidAt;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public PaymentEntity() {}

    public PaymentEntity(
            String id,
            String reservationId,
            String customerId,
            BigDecimal amount,
            PaymentStatus status,
            Instant paidAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Gettery i settery (lub Lombok)
}
