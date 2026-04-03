package car.rental.app.adapter.out.rest;

import car.rental.app.adapter.out.rest.dto.CarResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CarServiceClient {

    private static final Logger log = LoggerFactory.getLogger(CarServiceClient.class);

    private final RestTemplate rest;

    public CarServiceClient(RestTemplate rest) {
        this.rest = rest;
    }

    public CarResponse getCar(String carId) {
        try {
            return rest.getForObject(
                    "http://car-service/cars/" + carId,
                    CarResponse.class
            );
        } catch (Exception e) {
            log.error("Failed to fetch car {} from car-service", carId, e);
            return null;
        }
    }

    public boolean isCarAvailable(String carId) {
        try {
            Boolean result = rest.getForObject(
                    "http://car-service/cars/" + carId + "/availability",
                    Boolean.class
            );
            return result != null && result;
        } catch (Exception e) {
            log.error("Failed to fetch availability for car {}", carId, e);
            return false;
        }
    }
}
