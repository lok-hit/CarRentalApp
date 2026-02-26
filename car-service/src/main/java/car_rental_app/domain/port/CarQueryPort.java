package car_rental_app.domain.port;

import car_rental_app.domain.model.Car;
import car_rental_app.domain.model.CarId;

import java.util.List;
import java.util.Optional;

public interface CarQueryPort {

    Optional<Car> findById(CarId id);

    List<Car> findAvailable();
}