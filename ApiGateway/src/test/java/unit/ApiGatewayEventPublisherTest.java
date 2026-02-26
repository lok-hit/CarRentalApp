package unit;

import domain.events.ApiGatewayEventPublisher;
import domain.events.ApiGatewayRequestEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ApiGatewayEventPublisherTest {

    private ApplicationEventPublisher eventPublisher;
    private ApiGatewayEventPublisher apiGatewayEventPublisher;

    @BeforeEach
    public void setup() {
        eventPublisher = mock(ApplicationEventPublisher.class);
        apiGatewayEventPublisher = new ApiGatewayEventPublisher(eventPublisher);
    }

    @Test
    void shouldPublishRequestEventWithCorrectData() {
        String path = "/api/cars";
        String userId = "user123";
        String method = "GET";
        String correlationId = "corr-456";

        apiGatewayEventPublisher.publishRequestEvent(path, userId, method, correlationId);

        ArgumentCaptor<ApiGatewayRequestEvent> eventCaptor = ArgumentCaptor.forClass(ApiGatewayRequestEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        ApiGatewayRequestEvent publishedEvent = eventCaptor.getValue();
        assertEquals(path, publishedEvent.getPath());
        assertEquals(userId, publishedEvent.getUserId());
        assertEquals(method, publishedEvent.getMethod());
    }

    @Test
    void shouldPublishEventWithCorrectSource() {
        String path = "/api/reservations";
        String userId = "user456";
        String method = "POST";
        String correlationId = "corr-789";

        apiGatewayEventPublisher.publishRequestEvent(path, userId, method, correlationId);

        ArgumentCaptor<ApiGatewayRequestEvent> eventCaptor = ArgumentCaptor.forClass(ApiGatewayRequestEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        ApiGatewayRequestEvent publishedEvent = eventCaptor.getValue();
        assertEquals(apiGatewayEventPublisher, publishedEvent.getSource());
    }

    @Test
    void shouldPublishEventWithNullValues() {
        String path = null;
        String userId = null;
        String method = null;
        String correlationId = null;

        apiGatewayEventPublisher.publishRequestEvent(path, userId, method, correlationId);

        ArgumentCaptor<ApiGatewayRequestEvent> eventCaptor = ArgumentCaptor.forClass(ApiGatewayRequestEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        ApiGatewayRequestEvent publishedEvent = eventCaptor.getValue();
        assertEquals(path, publishedEvent.getPath());
        assertEquals(userId, publishedEvent.getUserId());
        assertEquals(method, publishedEvent.getMethod());
    }

    @Test
    void shouldPublishEventWithEmptyStrings() {
        String path = "";
        String userId = "";
        String method = "";
        String correlationId = "";

        apiGatewayEventPublisher.publishRequestEvent(path, userId, method, correlationId);

        ArgumentCaptor<ApiGatewayRequestEvent> eventCaptor = ArgumentCaptor.forClass(ApiGatewayRequestEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        ApiGatewayRequestEvent publishedEvent = eventCaptor.getValue();
        assertEquals(path, publishedEvent.getPath());
        assertEquals(userId, publishedEvent.getUserId());
        assertEquals(method, publishedEvent.getMethod());
    }

    @Test
    void shouldPublishMultipleEvents() {
        String path1 = "/api/cars";
        String userId1 = "user111";
        String method1 = "GET";
        String correlationId1 = "corr-111";

        String path2 = "/api/reservations";
        String userId2 = "user222";
        String method2 = "POST";
        String correlationId2 = "corr-222";

        apiGatewayEventPublisher.publishRequestEvent(path1, userId1, method1, correlationId1);
        apiGatewayEventPublisher.publishRequestEvent(path2, userId2, method2, correlationId2);

        ArgumentCaptor<ApiGatewayRequestEvent> eventCaptor = ArgumentCaptor.forClass(ApiGatewayRequestEvent.class);
        verify(eventPublisher, times(2)).publishEvent(eventCaptor.capture());

        var publishedEvents = eventCaptor.getAllValues();
        
        ApiGatewayRequestEvent firstEvent = publishedEvents.get(0);
        assertEquals(path1, firstEvent.getPath());
        assertEquals(userId1, firstEvent.getUserId());
        assertEquals(method1, firstEvent.getMethod());

        ApiGatewayRequestEvent secondEvent = publishedEvents.get(1);
        assertEquals(path2, secondEvent.getPath());
        assertEquals(userId2, secondEvent.getUserId());
        assertEquals(method2, secondEvent.getMethod());
    }
}
