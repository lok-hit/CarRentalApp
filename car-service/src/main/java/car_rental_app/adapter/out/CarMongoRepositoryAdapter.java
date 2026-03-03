package car_rental_app.adapter.out;

import car_rental_app.adapter.out.persistence.document.CarDocument;
import car_rental_app.adapter.out.persistence.document.StatusDocument;
import car_rental_app.adapter.out.persistence.mapper.CarMongoMapper;
import car_rental_app.adapter.out.persistence.repository.CarMongoRepository;
import car_rental_app.domain.model.AvailabilityStatus;
import car_rental_app.domain.model.Car;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.port.CarRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.MDC;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Primary
@Component
public class CarMongoRepositoryAdapter implements CarRepository {
    private static final Logger log = Logger.getLogger(CarMongoRepositoryAdapter.class.getName());
    private final CarMongoRepository mongo;

    public CarMongoRepositoryAdapter(CarMongoRepository mongo) {
        this.mongo = mongo;
    }

    private String trace() {
        return "trace=" + MDC.get("traceId") + " span=" + MDC.get("spanId");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Car> findById(CarId id) {
        log.fine(() -> "[" + trace() + "] Mongo: findById id=" + id.value());
        return mongo.findById(id.value()).map(CarMongoMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> findAvailable() {
        log.fine(() -> "[" + trace() + "] Mongo: findAvailable");
        return mongo.findByStatus(StatusDocument.AVAILABLE).stream().map(CarMongoMapper::toDomain).toList();
    }

    @Override
    @Transactional
    public Car save(Car car) {
        log.fine(() -> "[" + trace() + "] Mongo: save id=" + car.id().value());
        CarDocument saved = mongo.save(CarMongoMapper.toDocument(car));
        return CarMongoMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteAll() {
        log.fine(() -> "[" + trace() + "] Mongo: DeleteAll");
        mongo.deleteAll();
    }
}
