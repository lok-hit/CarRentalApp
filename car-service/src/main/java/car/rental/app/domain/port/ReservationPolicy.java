package car.rental.app.domain.port;

import car.rental.app.domain.model.Car;

public interface ReservationPolicy { boolean canBeReserved(Car car); }
