package car_rental_app.adapter.out.persistence.repository;

import car_rental_app.adapter.out.persistence.document.CarDocument;
import car_rental_app.adapter.out.persistence.document.StatusDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CarMongoRepository extends MongoRepository<CarDocument, String> {
    /**
 * Finds all car documents that have the specified status.
 *
 * @param status the StatusDocument to filter cars by
 * @return a List of CarDocument objects matching the provided status, or an empty list if none match
 */
List<CarDocument> findByStatus(StatusDocument status);
}