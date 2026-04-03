package car.rental.app.payment.adapter.out.audit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataPaymentAuditRepository
        extends MongoRepository<PaymentAuditDocument, String> {
}
