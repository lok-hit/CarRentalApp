package car.rental.app.monitoring.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI monitoringOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Monitoring Service API")
                        .version("v1")
                        .description("Audit logging and trace correlation — reactive WebFlux, MongoDB"))
                .externalDocs(new ExternalDocumentation()
                        .description("API Gateway")
                        .url("http://api-gateway:8080"));
    }
}
