package car.rental.app.domain.port;

import car.rental.app.domain.model.Reservation;
import car.rental.app.domain.model.ReservationId;

import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Reservation findById(ReservationId id);

    List<Reservation> findActiveByCarId(String carId);
}
