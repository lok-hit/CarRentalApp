package car_rental_app.domain.port;

import car_rental_app.domain.model.Car;
import car_rental_app.domain.model.CarId;

import java.util.List;
import java.util.Optional;

public interface CarRepository {
    Car save(Car car);

    /**
 * Retrieve a car by its identifier.
 *
 * @param id the identifier of the car to retrieve
 * @return an Optional containing the matching Car if found, otherwise an empty Optional
 */
Optional<Car> findById(CarId id);

    List<Car> findAvailable();
    void deleteAll();
}