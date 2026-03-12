package unit;

import car_rental_app.bff.dto.CarDto;
import car_rental_app.bff.dto.DashboardDto;
import car_rental_app.bff.dto.UserProfileDto;
import car_rental_app.bff.rest.BFFRestController;
import car_rental_app.infrastructure.clients.CarsClient;
import car_rental_app.infrastructure.clients.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class BFFRestControllerTest {

    private CarsClient carsClient;
    private UserClient userClient;
    private BFFRestController controller;
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        carsClient = Mockito.mock(CarsClient.class);
        userClient = Mockito.mock(UserClient.class);
        controller = new BFFRestController(carsClient, userClient);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldReturnDashboardDataSuccessfully() {
        // given
        List<CarDto> cars = List.of(
                new CarDto("1", "Sedan", "Toyota", "Camry", 2020,
                        "ABC123", 54332, true),
                new CarDto("2", "SUV", "BMW", "X5", 2022,
                        "XYZ789", 23456, true)
        );

        UserProfileDto user = new UserProfileDto(
                "u1", "mateusz",
                "mateusz@example.com", "Mateusz", "Tucholski", 5,
                false
        );

        when(carsClient.getAvailableCars()).thenReturn(Mono.just(cars));
        when(userClient.getUserProfile()).thenReturn(Mono.just(user));

        // when & then
        webTestClient.get()
                .uri("/bff/dashboard")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.availableCars").isArray()
                .jsonPath("$.availableCars.length()").isEqualTo(2)
                .jsonPath("$.availableCars[0].id").isEqualTo("1")
                .jsonPath("$.availableCars[0].brand").isEqualTo("Toyota")
                .jsonPath("$.availableCars[1].id").isEqualTo("2")
                .jsonPath("$.availableCars[1].brand").isEqualTo("BMW")
                .jsonPath("$.userProfile.username").isEqualTo("mateusz")
                .jsonPath("$.userProfile.email").isEqualTo("mateusz@example.com");

        verify(carsClient, times(1)).getAvailableCars();
        verify(userClient, times(1)).getUserProfile();
    }

    @Test
    void shouldReturnEmptyDashboardWhenNoData() {
        // given
        when(carsClient.getAvailableCars()).thenReturn(Mono.just(List.of()));
        when(userClient.getUserProfile()).thenReturn(Mono.just(
                new UserProfileDto("u1", "test", "test@example.com",
                        "Test", "User", 0, false)
        ));

        // when & then
        webTestClient.get()
                .uri("/bff/dashboard")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.availableCars").isArray()
                .jsonPath("$.availableCars.length()").isEqualTo(0)
                .jsonPath("$.userProfile.username").isEqualTo("test");

        verify(carsClient, times(1)).getAvailableCars();
        verify(userClient, times(1)).getUserProfile();
    }

    @Test
    void shouldReturnErrorWhenCarsClientFails() {
        // given
        when(carsClient.getAvailableCars()).thenReturn(Mono.error(new RuntimeException("Cars service unavailable")));
        when(userClient.getUserProfile()).thenReturn(Mono.just(
                new UserProfileDto("u1", "test", "test@example.com",
                        "Test", "User", 0, false)
        ));

        // when & then
        webTestClient.get()
                .uri("/bff/dashboard")
                .exchange()
                .expectStatus().is5xxServerError();

        verify(carsClient, times(1)).getAvailableCars();
        verify(userClient, times(1)).getUserProfile();
    }

    @Test
    void shouldReturnErrorWhenUserClientFails() {
        // given
        when(carsClient.getAvailableCars()).thenReturn(Mono.just(List.of()));
        when(userClient.getUserProfile()).thenReturn(Mono.error(new RuntimeException("User service unavailable")));

        // when & then
        webTestClient.get()
                .uri("/bff/dashboard")
                .exchange()
                .expectStatus().is5xxServerError();

        verify(carsClient, times(1)).getAvailableCars();
        verify(userClient, times(1)).getUserProfile();
    }

    @Test
    void shouldReturnErrorWhenBothClientsFail() {
        // given
        when(carsClient.getAvailableCars()).thenReturn(Mono.error(new RuntimeException("Cars service error")));
        when(userClient.getUserProfile()).thenReturn(Mono.error(new RuntimeException("User service error")));

        // when & then
        webTestClient.get()
                .uri("/bff/dashboard")
                .exchange()
                .expectStatus().is5xxServerError();

        verify(carsClient, times(1)).getAvailableCars();
        verify(userClient, times(1)).getUserProfile();
    }

    @Test
    void shouldVerifyDashboardMethodReturnsCorrectMono() {
        // given
        List<CarDto> cars = List.of(
                new CarDto("1", "Sedan", "Toyota", "Camry", 2020,
                        "ABC123", 54332, true)
        );

        UserProfileDto user = new UserProfileDto(
                "u1", "mateusz",
                "mateusz@example.com", "Mateusz", "Tucholski", 5,
                false
        );

        when(carsClient.getAvailableCars()).thenReturn(Mono.just(cars));
        when(userClient.getUserProfile()).thenReturn(Mono.just(user));

        // when
        Mono<DashboardDto> result = controller.dashboard();

        // then
        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.availableCars().size() == 1 &&
                                dto.availableCars().get(0).id().equals("1") &&
                                dto.userProfile().username().equals("mateusz") &&
                                dto.userProfile().email().equals("mateusz@example.com")
                )
                .verifyComplete();

        verify(carsClient, times(1)).getAvailableCars();
        verify(userClient, times(1)).getUserProfile();
    }
}
