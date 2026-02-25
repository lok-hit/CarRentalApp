import bff.dto.CarDto;
import bff.dto.DashboardDto;
import bff.dto.UserProfileDto;
import bff.service.BffAggregationService;
import infrastructure.clients.CarsClient;
import infrastructure.clients.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

public class BffAggregationServiceTest {

    private CarsClient carsClient;
    private UserClient userClient;
    private BffAggregationService aggregationService;

    @BeforeEach
    public void setup() {

        carsClient = Mockito.mock(CarsClient.class);
        userClient = Mockito.mock(UserClient.class);
        aggregationService = new BffAggregationService(carsClient, userClient);
    }

    @Test
    void shouldReturnDashboardData() {
        // given
        List<CarDto> cars = List.of(
                new CarDto("1", "Sedan", "Toyota", "Camry", 2020,
                        "myuCar", 54332,true),
                new CarDto("2", "SUV", "BMW", "X5", 2022,
                        "kk09", 23456, true)
        );

        UserProfileDto user = new UserProfileDto(
                "u1", "mateusz",
                "mateusz@example.com", "Mateusz", "Tucholski", 5,
                false);

        when(carsClient.getAvailableCars()).thenReturn(Mono.just(cars));
        when(userClient.getUserProfile()).thenReturn(Mono.just(user));

        // when
        Mono<DashboardDto> result = aggregationService.getDashboard();

        // then
        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.availableCars().size() == 2 &&
                                dto.userProfile().username().equals("mateusz")
                )
                .verifyComplete();

        verify(carsClient, times(1)).getAvailableCars();
        verify(userClient, times(1)).getUserProfile();
    }

    @Test
    void shouldPropagateErrorWhenCarsClientFails() {
        when(carsClient.getAvailableCars()).thenReturn(Mono.error(new RuntimeException("Cars error")));
        when(userClient.getUserProfile()).thenReturn(Mono.just(
                new UserProfileDto("u1",
                        "mateusz", "m@example.com",
                        "Mateusz", "Tucholski",
                        5, false)
        ));

        StepVerifier.create(aggregationService.getDashboard())
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("Cars error"))
                .verify();
    }

    @Test
    void shouldPropagateErrorWhenUsersClientFails() {
        when(carsClient.getAvailableCars()).thenReturn(Mono.just(List.of()));
        when(userClient.getUserProfile()).thenReturn(Mono.error(new RuntimeException("User error")));

        StepVerifier.create(aggregationService.getDashboard())
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("User error"))
                .verify();
    }
}

