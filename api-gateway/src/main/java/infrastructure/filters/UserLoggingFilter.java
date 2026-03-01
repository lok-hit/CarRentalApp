package infrastructure.filters;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class UserLoggingFilter implements WebFilter {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext -> {
                    var auth = securityContext.getAuthentication();

                    if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
                        String userId = jwt.getClaimAsString("sub");
                        String roles = String.join(",", auth.getAuthorities().stream()
                                .map(a -> a.getAuthority()).toList());
                        return chain.filter(exchange).contextWrite(ctx ->
                                ctx.put("userId", userId).put("roles", roles));
                    }
                    return chain.filter(exchange);
                });
    }
}
