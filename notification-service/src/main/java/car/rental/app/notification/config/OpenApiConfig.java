package car.rental.app.notification.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Notification Service API")
                        .version("v1")
                        .description("Email/SMS/push notifications for reservation lifecycle events"))
                .externalDocs(new ExternalDocumentation()
                        .description("API Gateway")
                        .url("http://api-gateway:8080"));
    }
}
