package car_rental_app.adapter.out;

import car_rental_app.adapter.out.persistence.document.CarDocument;
import car_rental_app.adapter.out.persistence.document.StatusDocument;
import car_rental_app.adapter.out.persistence.mapper.CarMongoMapper;
import car_rental_app.adapter.out.persistence.repository.CarMongoRepository;
import car_rental_app.domain.model.Car;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.port.CarQueryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CarMongoQueryAdapter implements CarQueryPort {
    
    private final CarMongoRepository mongoRepository;

    public CarMongoQueryAdapter(CarMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Optional<Car> findById(CarId id) {
        return mongoRepository.findById(id.value())
                .map(CarMongoMapper::toDomain);
    }

    @Override
    public List<Car> findAvailable() {
        return mongoRepository.findByStatus(StatusDocument.AVAILABLE)
                .stream()
                .map(CarMongoMapper::toDomain)
                .toList();
    }
}
