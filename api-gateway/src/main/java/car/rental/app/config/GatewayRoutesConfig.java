package car.rental.app.config;


import car.rental.app.infrastructure.filters.DynamicRateLimiterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
@Configuration
public class GatewayRoutesConfig {

    private final DynamicRateLimiterFactory rateLimiterFactory;
    private final KeyResolver userKeyResolver;

    public GatewayRoutesConfig(DynamicRateLimiterFactory rateLimiterFactory,
                               KeyResolver userKeyResolver) {
        this.rateLimiterFactory = rateLimiterFactory;
        this.userKeyResolver = userKeyResolver;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("cars_route", r -> r.path("/cars/**")
                        .filters(f -> f
                                .requestRateLimiter(c -> {
                                    c.setRateLimiter(rateLimiterFactory.forRoute("cars"));
                                    c.setKeyResolver(userKeyResolver);
                                })
                                .circuitBreaker(c -> {
                                    c.setName("carsServiceCircuitBreaker");
                                    c.setFallbackUri("forward:/fallback/cars");
                                })
                                .retry(retry -> retry
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setStatuses(
                                                HttpStatus.BAD_GATEWAY,
                                                HttpStatus.GATEWAY_TIMEOUT,
                                                HttpStatus.INTERNAL_SERVER_ERROR
                                        )
                                )
                        )
                        .uri("http://cars-service"))

                .build();
    }
}
