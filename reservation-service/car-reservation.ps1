$base = "src/main/java/car_rental_app"

$folders = @(
    "$base/domain/model",
    "$base/domain/event",
    "$base/domain/port",
    "$base/application/command",
    "$base/application/service",
    "$base/application/service/saga",
    "$base/adapter/in/rest",
    "$base/adapter/in/messaging",
    "$base/adapter/out/persistence/entity",
    "$base/adapter/out/persistence/repository",
    "$base/adapter/out/messaging",
    "$base/adapter/out/outbox"
)

foreach ($f in $folders) {
    if (-not (Test-Path $f)) {
        New-Item -ItemType Directory -Force -Path $f | Out-Null
    }
}

$classes = @{
    "$base/domain/model/Reservation.java" = "package car_rental_app.domain.model; public class Reservation {}"
    "$base/domain/model/Payment.java" = "package car_rental_app.domain.model; public class Payment {}"
    "$base/domain/event/ReservationCreatedEvent.java" = "package car_rental_app.domain.event; public class ReservationCreatedEvent {}"
    "$base/domain/event/ReservationConfirmedEvent.java" = "package car_rental_app.domain.event; public class ReservationConfirmedEvent {}"
    "$base/domain/event/ReservationCancelledEvent.java" = "package car_rental_app.domain.event; public class ReservationCancelledEvent {}"
    "$base/domain/event/PaymentCompletedEvent.java" = "package car_rental_app.domain.event; public class PaymentCompletedEvent {}"
    "$base/domain/event/PaymentFailedEvent.java" = "package car_rental_app.domain.event; public class PaymentFailedEvent {}"
    "$base/domain/port/ReservationRepository.java" = "package car_rental_app.domain.port; public interface ReservationRepository {}"
    "$base/domain/port/EventPublisher.java" = "package car_rental_app.domain.port; public interface EventPublisher {}"
    "$base/domain/port/OutboxEventStore.java" = "package car_rental_app.domain.port; public interface OutboxEventStore {}"
    "$base/application/command/CreateReservationCommand.java" = "package car_rental_app.application.command; public class CreateReservationCommand {}"
    "$base/application/service/ReservationApplicationService.java" = "package car_rental_app.application.service; public class ReservationApplicationService {}"
    "$base/application/service/saga/ReservationSagaHandler.java" = "package car_rental_app.application.service.saga; public class ReservationSagaHandler {}"
    "$base/adapter/in/rest/ReservationCommandController.java" = "package car_rental_app.adapter.in.rest; public class ReservationCommandController {}"
    "$base/adapter/in/rest/ReservationQueryController.java" = "package car_rental_app.adapter.in.rest; public class ReservationQueryController {}"
    "$base/adapter/in/messaging/PaymentListener.java" = "package car_rental_app.adapter.in.messaging; public class PaymentListener {}"
    "$base/adapter/out/persistence/entity/ReservationEntity.java" = "package car_rental_app.adapter.out.persistence.entity; public class ReservationEntity {}"
    "$base/adapter/out/persistence/entity/PaymentEntity.java" = "package car_rental_app.adapter.out.persistence.entity; public class PaymentEntity {}"
    "$base/adapter/out/persistence/repository/ReservationRepositoryAdapter.java" = "package car_rental_app.adapter.out.persistence.repository; public class ReservationRepositoryAdapter {}"
    "$base/adapter/out/messaging/KafkaEventPublisher.java" = "package car_rental_app.adapter.out.messaging; public class KafkaEventPublisher {}"
    "$base/adapter/out/outbox/OutboxEventEntity.java" = "package car_rental_app.adapter.out.outbox; public class OutboxEventEntity {}"
}

foreach ($path in $classes.Keys) {
    if (-not (Test-Path $path)) {
        Set-Content -Path $path -Value $classes[$path]
    }
}
