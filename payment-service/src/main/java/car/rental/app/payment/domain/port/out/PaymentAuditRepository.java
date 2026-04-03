package car.rental.app.payment.domain.port.out;

import car.rental.app.payment.domain.audit.PaymentAuditEntry;

public interface PaymentAuditRepository {

    void save(PaymentAuditEntry entry);
}
