package car_rental_app.application.service;

import car_rental_app.domain.model.Car;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.port.CarQueryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QueryService {

    private final CarQueryPort carQueryPort;

    @Autowired
    public QueryService(CarQueryPort carQueryPort) {
        this.carQueryPort = carQueryPort;
    }

    public List<Car> getAvailableCars() {
        return carQueryPort.findAvailable();
    }

    public Optional<Car> getCarById(CarId id) {
        return carQueryPort.findById(id);
    }
}
