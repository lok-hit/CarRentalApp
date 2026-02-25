package domain.events;

import org.springframework.context.ApplicationEvent;

public class ApiGatewayRequestEvent extends ApplicationEvent {

    private final String path;
    private final String userId;
    private final String method;

    public ApiGatewayRequestEvent(Object source, String path, String userId, String method) {
        super(source);
        this.path = path;
        this.userId = userId;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getUserId() {
        return userId;
    }

    public String getMethod() {
        return method;
    }
}

