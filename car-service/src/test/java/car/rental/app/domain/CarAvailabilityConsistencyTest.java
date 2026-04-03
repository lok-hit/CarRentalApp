package car.rental.app.domain;


import car.rental.app.CarServiceMain;
import car.rental.app.BaseIntegrationTest;
import car.rental.app.adapter.out.CarMongoRepositoryAdapter;
import car.rental.app.application.service.saga.ReservationSagaHandler;
import car.rental.app.domain.model.*;
import car.rental.app.domain.saga.event.*;
import car_rental_app.domain.model.*;
import car_rental_app.domain.saga.event.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CarServiceMain.class)
class CarAvailabilityConsistencyTest extends BaseIntegrationTest {

    private Car car = new Car(
            new CarId("c1"),
            CarCategory.SEDAN,
            new Price(BigDecimal.valueOf(1000))
    );

    @Autowired
    CarMongoRepositoryAdapter carRepository;

    @Autowired
    ReservationSagaHandler sagaHandler;

    @BeforeEach
    void setup() {
        carRepository.deleteAll();
        carRepository.save(car);
    }

    @Test
     void reservationCreated_shouldMakeCarUnavailable() {

        sagaHandler.onReservationCreated(new ReservationCreatedEvent("r1", car.id(), "u1"));

        Optional<Car> found = carRepository.findById(car.id());

        assertThat(found).isPresent();
        assertThat(found.get().status()).isEqualTo(AvailabilityStatus.UNAVAILABLE);
    }

    @Test
    void paymentCompleted_shouldKeepCarUnavailable() {
        sagaHandler.onPaymentConfirmed(new PaymentConfirmedEvent("1",
                car.id().value(), "d", "1"));
        Optional<Car> found = carRepository.findById(car.id());
        assertThat(found).isPresent();
        assertThat(found.get().status()).isEqualTo(AvailabilityStatus.UNAVAILABLE);
    }

    @Test
    void reservationConfirmed_shouldKeepCarUnavailable() {
        sagaHandler.onReservationConfirmed(new ReservationConfirmedEvent("r1", "c1", "u1"));
        Optional<Car> found = carRepository.findById(car.id());
        assertThat(found).isPresent();
        assertThat(found.get().status()).isEqualTo(AvailabilityStatus.UNAVAILABLE);
    }

    @Test
    void paymentFailed_shouldMakeCarAvailable() {
        sagaHandler.onReservationFailed(new PaymentFailedEvent("r1", car.id(), "u1"));
        Optional<Car> found = carRepository.findById(car.id());
        assertThat(found).isPresent();
        assertThat(found.get().status()).isEqualTo(AvailabilityStatus.AVAILABLE);
    }

    @Test
    void reservationCancelled_shouldMakeCarAvailable() {
        sagaHandler.onReservationCancelled(new ReservationCancelledEvent("r1", car.id(), "u8", Instant.now()));
        Optional<Car> found = carRepository.findById(car.id());
        assertThat(found).isPresent();
        assertThat(found.get().status()).isEqualTo(AvailabilityStatus.AVAILABLE);
    }
}

