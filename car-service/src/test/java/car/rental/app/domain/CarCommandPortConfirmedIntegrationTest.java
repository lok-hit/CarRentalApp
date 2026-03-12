package car.rental.app.domain;

import car.rental.app.CarServiceMain;
import car.rental.app.BaseIntegrationTest;
import car.rental.app.adapter.out.CarMongoRepositoryAdapter;
import car.rental.app.domain.model.*;
import car_rental_app.domain.model.*;
import car.rental.app.application.port.CarCommandPort;
import car.rental.app.domain.saga.event.ReservationConfirmedEvent;
import car.rental.app.application.service.saga.ReservationSagaHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CarServiceMain.class)
class CarCommandPortConfirmedIntegrationTest extends BaseIntegrationTest {

    @Autowired
    CarMongoRepositoryAdapter carRepository;

    @Autowired
    CarCommandPort carCommandPort;

    @Autowired
    ReservationSagaHandler sagaHandler;

    @Test
    void shouldMarkCarUnavailableOnReservationConfirmed() {
        // given –
        Car car = new Car(new CarId("c1"), CarCategory.SEDAN, new Price(BigDecimal.valueOf(1000)));
        carRepository.save(car);

        ReservationConfirmedEvent event =
                new ReservationConfirmedEvent("r1", "c1", "u1");

        // when
        sagaHandler.onReservationConfirmed(event);

        // then
        Optional<Car> updated = carRepository.findById(new CarId(event.getCarId()));

        assertThat(updated)
                .isPresent()
                .get()
                .extracting(Car::status)
                .isEqualTo(AvailabilityStatus.UNAVAILABLE);
    }
}

