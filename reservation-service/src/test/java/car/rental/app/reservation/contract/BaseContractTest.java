package car.rental.app.reservation.contract;


import car.rental.app.ReservationServiceApplication;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(
        classes = ReservationServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class BaseContractTest {

    @LocalServerPort
    protected int port;

    @BeforeEach
    void setup() {
        System.setProperty("stubrunner.running.port", String.valueOf(port));
    }

    public void triggerReservationCreated() {

    }

    public void triggerReservationCancelled() {
    }

}
