package domain;


import car_rental_app.BaseIntegrationTest;
import car_rental_app.adapter.out.persistence.CarRepositoryAdapter;
import car_rental_app.application.service.saga.ReservationSagaHandler;
import car_rental_app.domain.model.*;
import car_rental_app.domain.saga.event.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CarAvailabilityConsistencyTest extends BaseIntegrationTest {

    private Car car = new Car(
            new CarId("c1"),
            CarCategory.SEDAN,
            new Price(BigDecimal.valueOf(1000))
    );

    @Autowired
    CarRepositoryAdapter carRepository;

    @Autowired
    ReservationSagaHandler sagaHandler;

    @BeforeEach
    void setup() {
        carRepository.DeleteAll();
        carRepository.save(car);
    }

    @Test
    public void reservationCreated_shouldMakeCarUnavailable() {

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
        sagaHandler.onReservationCancelled(new ReservationCancelledEvent("r1", car.id()));
        Optional<Car> found = carRepository.findById(car.id());
        assertThat(found).isPresent();
        assertThat(found.get().status()).isEqualTo(AvailabilityStatus.AVAILABLE);
    }
}

