package car.rental.app.domain.port;

import car.rental.app.domain.model.Car;
import car.rental.app.domain.model.CarId;

import java.util.List;
import java.util.Optional;

public interface CarQueryPort {

    Optional<Car> findById(CarId id);

    List<Car> findAvailable();
}