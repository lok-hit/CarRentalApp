package domain.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ApiGatewayEventPublisher {
    private final ApplicationEventPublisher publisher;

    public ApiGatewayEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }


    public void publishRequestEvent(String path, String userId, String method, String correlationId) {
        publisher.publishEvent(new ApiGatewayRequestEvent(this, path, userId, method));
    }
}

