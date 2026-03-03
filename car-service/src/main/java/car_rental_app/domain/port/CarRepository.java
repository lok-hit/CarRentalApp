package car_rental_app.domain.port;

import car_rental_app.domain.model.Car;
import car_rental_app.domain.model.CarId;

import java.util.List;
import java.util.Optional;

public interface CarRepository {
    Car save(Car car);

    Optional<Car> findById(CarId id);

    List<Car> findAvailable();
    void deleteAll();
}