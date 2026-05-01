package car.rental.app.bff.service;

import car.rental.app.bff.dto.DashboardDto;
import car.rental.app.infrastructure.clients.CarsClient;
import car.rental.app.infrastructure.clients.UserClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BffAggregationService {

    private final CarsClient carsClient;
    private final UserClient usersClient;

    public BffAggregationService(CarsClient carsClient, UserClient usersClient) {
        this.carsClient = carsClient;
        this.usersClient = usersClient;
    }

    public Mono<DashboardDto> getDashboard(String bearerToken) {
        return Mono.zip(
                carsClient.getAvailableCars(bearerToken),
                usersClient.getUserProfile(bearerToken)
        ).map(tuple -> new DashboardDto(tuple.getT1(), tuple.getT2()));
    }
}
