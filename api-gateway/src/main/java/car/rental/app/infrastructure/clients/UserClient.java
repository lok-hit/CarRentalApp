package car.rental.app.infrastructure.clients;

import car.rental.app.bff.dto.UserProfileDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserClient {

    private final WebClient webClient;
    private static final UserProfileDto EMPTY = new UserProfileDto("", "unknown", "", "", "", 0, false);

    public UserClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://user-profile-service").build();
    }

    public Mono<UserProfileDto> getUserProfile(String bearerToken) {
        return webClient.get()
                .uri("/users")
                .header("Authorization", bearerToken)
                .retrieve()
                .bodyToFlux(UserProfileDto.class)
                .next()
                .defaultIfEmpty(EMPTY)
                .onErrorReturn(EMPTY);
    }
}
