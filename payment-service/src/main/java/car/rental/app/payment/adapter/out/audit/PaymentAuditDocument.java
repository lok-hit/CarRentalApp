package car.rental.app.payment.adapter.out.audit;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document("payment_audit")
public class PaymentAuditDocument {

    @Id
    private String id;

    private String paymentId;
    private String reservationId;
    private String customerId;
    private String provider;
    private String requestPayload;
    private String responsePayload;
    private boolean success;
    private String failureReason;
    private Instant occurredAt;
    private String traceId;
    private String correlationId;
}