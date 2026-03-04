package car_rental_app.domain.port;

import car_rental_app.domain.model.Reservation;
import car_rental_app.domain.model.ReservationId;

import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Reservation findById(ReservationId id);

    List<Reservation> findActiveByCarId(String carId);
}
