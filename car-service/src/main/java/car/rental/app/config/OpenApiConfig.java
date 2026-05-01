package car.rental.app.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Service API")
                        .version("v1")
                        .description("Vehicle catalog, availability and pricing — hexagonal architecture, CQRS"))
                .externalDocs(new ExternalDocumentation()
                        .description("API Gateway")
                        .url("http://api-gateway:8080"));
    }
}
