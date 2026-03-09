package car.rental.app.payment.adapter.out.idempotency;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "idempotency_keys")
public class IdempotencyKeyDocument {

    @Indexed(unique = true)
    private String key;

    @CreatedDate
    @Indexed(expireAfterSeconds = 86400) // 24h TTL
    private Instant createdAt;

    public IdempotencyKeyDocument(String key) {
        this.key = key;
        this.createdAt = Instant.now();
    }

    public String getKey() {
        return key;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}