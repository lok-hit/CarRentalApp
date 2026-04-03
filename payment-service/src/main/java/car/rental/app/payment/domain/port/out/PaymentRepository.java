package car.rental.app.payment.domain.port.out;

import car.rental.app.payment.domain.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(String id);

    List<Payment> findByReservationId(String reservationId);

    List<Payment> findByCustomerId(String customerId);
}
