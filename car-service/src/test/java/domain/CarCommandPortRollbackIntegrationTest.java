package domain;

import car_rental_app.BaseIntegrationTest;
import car_rental_app.adapter.out.CarMongoRepositoryAdapter;
import car_rental_app.application.service.saga.ReservationSagaHandler;
import car_rental_app.domain.model.*;
import car_rental_app.application.port.CarCommandPort;
import car_rental_app.domain.saga.event.PaymentFailedEvent;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CarCommandPortRollbackIntegrationTest extends BaseIntegrationTest {
    @Autowired
    CarMongoRepositoryAdapter carRepository;
    @Autowired
    CarCommandPort carCommandPort;
    @Autowired
    ReservationSagaHandler sagaHandler;

    @Test
    public void shouldMarkCarAvailableOnPaymentFailedRollback() {

        Car car = new Car(new CarId("c1"), CarCategory.ELECTRIC, new Price(BigDecimal.valueOf(100.00)));
        carRepository.save(car);

        PaymentFailedEvent event = new PaymentFailedEvent("r1", new CarId("c1"), "u9");

        // when

        sagaHandler.onReservationFailed(event);

        // then
        Optional<Car> updated = carRepository.findById(new CarId("c1"));

        assertThat(updated).isPresent();
        assertThat(updated.get().status().equals(AvailabilityStatus.AVAILABLE));
    }
}