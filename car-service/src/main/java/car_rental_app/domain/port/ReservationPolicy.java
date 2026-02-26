package car_rental_app.domain.port;

import car_rental_app.domain.model.Car;

public interface ReservationPolicy { boolean canBeReserved(Car car); }
