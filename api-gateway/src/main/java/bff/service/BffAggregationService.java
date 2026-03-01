package bff.service;

import bff.dto.DashboardDto;
import infrastructure.clients.CarsClient;
import infrastructure.clients.UserClient;
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

    public Mono<DashboardDto> getDashboard() {
        return Mono.zip(
                carsClient.getAvailableCars(),
                usersClient.getUserProfile()
        ).map(tuple -> new DashboardDto(tuple.getT1(), tuple.getT2()));
    }
}