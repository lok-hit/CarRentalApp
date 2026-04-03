package car.rental.app.adapter.out.messaging.outbox;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OutboxEventRepository extends MongoRepository<OutboxEventDocument, String> {
    List<OutboxEventDocument> findByProcessedFalseOrderByCreatedAtAsc();
    List<OutboxEventDocument> findByStatus(String status);

}