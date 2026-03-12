package car.rental.app.adapter.out.persistence.repository;

import car.rental.app.adapter.out.persistence.entity.ReservationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpringDataReservationRepository extends MongoRepository<ReservationEntity, String> {

    List<ReservationEntity> findByCustomerId(String customerId);

    List<ReservationEntity> findByCarId(String carId);
}