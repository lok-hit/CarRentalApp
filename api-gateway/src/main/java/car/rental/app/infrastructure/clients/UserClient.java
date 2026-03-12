package car.rental.app.infrastructure.clients;

import car.rental.app.bff.dto.UserProfileDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class UserClient {
    private final WebClient webClient;

    public UserClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://users-service").build();
    }

    public Mono<UserProfileDto> getUserProfile() {
        return webClient.get()
                .uri("/users/me")
                .retrieve()
                .bodyToMono(UserProfileDto.class);
    }
}


