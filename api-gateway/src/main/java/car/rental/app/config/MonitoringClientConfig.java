package car.rental.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MonitoringClientConfig {
    @Bean
    public WebClient monitoringWebClient(@Value("${monitoring.service.url}") String monitoringServiceUrl) {
        return WebClient.builder().baseUrl(monitoringServiceUrl).build();
    }
}
