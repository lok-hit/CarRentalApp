package car.rental.app.adapter.out.persistence;

import car.rental.app.adapter.out.persistence.entity.CarEntity;
import car.rental.app.adapter.out.persistence.mapper.CarMapper;
import car.rental.app.adapter.out.persistence.repository.CarJpaRepository;
import car.rental.app.domain.model.AvailabilityStatus;
import car.rental.app.domain.model.Car;
import car.rental.app.domain.model.CarId;
import car.rental.app.domain.port.CarRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.MDC;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
@ConditionalOnBean(CarJpaRepository.class)
public class CarRepositoryAdapter implements CarRepository {
    private static final Logger log = Logger.getLogger(CarRepositoryAdapter.class.getName());
    private final CarJpaRepository jpa;

    public CarRepositoryAdapter(CarJpaRepository jpa) {
        this.jpa = jpa;
    }

    private String trace() {
        return "trace=" + MDC.get("traceId") + " span=" + MDC.get("spanId");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Car> findById(CarId id) {
        log.fine(() -> "[" + trace() + "] JPA: findByIdWithRelations id=" + id.value());
        CarEntity entity = jpa.findByIdWithRelations(id.value());
        return Optional.ofNullable(entity).map(CarMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> findAvailable() {
        log.fine(() -> "[" + trace() + "] JPA: findAvailableWithRelations");
        return jpa.findByStatusWithRelations(AvailabilityStatus.AVAILABLE.name())
                .stream()
                .map(CarMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAll() {

        log.fine(() -> "[" + trace() + "] JPA: deleteAll");
        jpa.deleteAll();
    }

    @Override
    @Transactional
    public Car save(Car car) {
        log.fine(() -> "[" + trace() + "] JPA: save id=" + car.id().value());
        return CarMapper.toDomain(jpa.save(CarMapper.toEntity(car)));
    }
}
