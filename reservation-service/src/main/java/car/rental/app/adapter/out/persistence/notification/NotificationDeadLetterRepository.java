package car.rental.app.adapter.out.persistence.notification;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationDeadLetterRepository
        extends MongoRepository<NotificationDeadLetterEntry, String> {}

