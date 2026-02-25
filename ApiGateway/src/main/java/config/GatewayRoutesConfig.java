package config;

import infrastructure.filters.JwtTokenRelayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("cars-service", r -> r
                        .path("/cars/**")
                        .filters(f -> f
                                .rewritePath("/cars/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(c -> c
                                        .setName("carsServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/cars"))
                                .retry( retry -> retry
                                        .setRetries(3)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR))
                                .requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter()))
                                .filter(new JwtTokenRelayFilter())
                        )
                        .uri("lb://cars-service")
                )

                .route("users-service", r -> r
                        .path("/users/**")
                        .filters(f -> f
                                .rewritePath("/users/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://users-service")
                )

                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }
}
