package infrastructure.filters;

import infrastructure.RateLimitProperties;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.stereotype.Component;

@Component
public class DynamicRateLimiterFactory {

    private final RateLimitProperties properties;

    public DynamicRateLimiterFactory(RateLimitProperties properties) {
        this.properties = properties;
    }

    public RedisRateLimiter forRoute(String routeId) {
        RateLimitProperties.Limit limit = properties.getLimits().get(routeId);

        if (limit == null) {
            return new RedisRateLimiter(10, 20); // default
        }

        return new RedisRateLimiter(
                limit.getReplenishRate(),
                limit.getBurstCapacity()
        );
    }
}

