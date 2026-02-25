package domain.audit.service;

import domain.audit.AuditEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuditService {
    private final WebClient monitoringWebClient;

    public AuditService(WebClient monitoringWebClient) {
        this.monitoringWebClient = monitoringWebClient;
    }

    public Mono<Void> send(AuditEvent event) {
        return monitoringWebClient.post().uri("/audit").bodyValue(event).retrieve()
                .bodyToMono(Void.class).onErrorResume(e -> Mono.empty());
    }
}
