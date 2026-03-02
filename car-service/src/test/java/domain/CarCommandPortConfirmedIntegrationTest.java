package domain;

import car_rental_app.BaseIntegrationTest;
import car_rental_app.adapter.out.CarMongoRepositoryAdapter;
import car_rental_app.domain.model.*;
import car_rental_app.application.port.CarCommandPort;
import car_rental_app.domain.saga.event.ReservationConfirmedEvent;
import car_rental_app.application.service.saga.ReservationSagaHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = car_rental_app.CarServiceMain.class)
public class CarCommandPortConfirmedIntegrationTest extends BaseIntegrationTest {

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

        assertThat(updated).isPresent();
        assertThat(updated.get().status().equals(AvailabilityStatus.UNAVAILABLE));
    }
}

