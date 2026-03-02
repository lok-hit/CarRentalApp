package car_rental_app.adapter.out.persistence.repository;

import car_rental_app.adapter.out.persistence.document.CarDocument;
import car_rental_app.adapter.out.persistence.document.StatusDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CarMongoRepository extends MongoRepository<CarDocument, String> {
    List<CarDocument> findByStatus(StatusDocument status);
}