package car.rental.app.bff.rest;

import car.rental.app.bff.dto.DashboardDto;
import car.rental.app.infrastructure.clients.CarsClient;
import car.rental.app.infrastructure.clients.UserClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff")
public class BFFRestController {

    private final CarsClient carsClient;
    private final UserClient userClient;

    public BFFRestController (CarsClient carsClient, UserClient usersClient){
        this.carsClient = carsClient;
        this.userClient = usersClient;
    }


    @GetMapping("/dashboard")
    public Mono<DashboardDto> dashboard() {
        return Mono.zip(
                carsClient.getAvailableCars(),
                userClient.getUserProfile()
        ).map(tuple -> new DashboardDto(tuple.getT1(), tuple.getT2()));
    }


}
