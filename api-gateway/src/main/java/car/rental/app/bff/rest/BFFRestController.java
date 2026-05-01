package car.rental.app.bff.rest;

import car.rental.app.bff.dto.DashboardDto;
import car.rental.app.infrastructure.clients.CarsClient;
import car.rental.app.infrastructure.clients.UserClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff")
public class BFFRestController {

    private final CarsClient carsClient;
    private final UserClient userClient;

    public BFFRestController(CarsClient carsClient, UserClient userClient) {
        this.carsClient = carsClient;
        this.userClient = userClient;
    }

    @GetMapping("/dashboard")
    public Mono<DashboardDto> dashboard(Authentication authentication) {
        String bearer = extractBearer(authentication);
        return Mono.zip(
                carsClient.getAvailableCars(bearer),
                userClient.getUserProfile(bearer)
        ).map(tuple -> new DashboardDto(tuple.getT1(), tuple.getT2()));
    }

    private String extractBearer(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return "Bearer " + jwt.getTokenValue();
        }
        return "";
    }
}
