package car.rental.app.infrastructure.clients;

import car.rental.app.bff.dto.CarDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CarsClient {

    private final WebClient webClient;

    public CarsClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://car-service").build();
    }

    public Mono<List<CarDto>> getAvailableCars(String bearerToken) {
        return webClient.get()
                .uri("/cars")
                .header("Authorization", bearerToken)
                .retrieve()
                .bodyToFlux(CarDto.class)
                .collectList()
                .onErrorReturn(List.of());
    }
}
