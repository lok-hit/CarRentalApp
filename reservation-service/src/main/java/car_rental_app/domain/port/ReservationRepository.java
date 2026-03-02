package car_rental_app.domain.port;

import car_rental_app.domain.model.Reservation;
import car_rental_app.domain.model.ReservationId;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Reservation findById(ReservationId id);
}
