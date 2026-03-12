package car.rental.app.adapter.out.persistence.notification;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationOutboxRepository extends MongoRepository<NotificationOutboxEntry, String> {}
