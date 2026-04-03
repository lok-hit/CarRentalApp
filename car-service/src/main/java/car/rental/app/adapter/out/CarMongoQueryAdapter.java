package car.rental.app.adapter.out;

import car.rental.app.adapter.out.persistence.document.StatusDocument;
import car.rental.app.adapter.out.persistence.mapper.CarMongoMapper;
import car.rental.app.adapter.out.persistence.repository.CarMongoRepository;
import car.rental.app.domain.model.Car;
import car.rental.app.domain.model.CarId;
import car.rental.app.domain.port.CarQueryPort;
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
