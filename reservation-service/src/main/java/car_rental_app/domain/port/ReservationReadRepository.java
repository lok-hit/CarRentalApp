package car_rental_app.domain.port;

import car_rental_app.domain.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationReadRepository {

    Optional<Reservation> findById(String reservationId);

    List<Reservation> findByCustomerId(String customerId);
}
