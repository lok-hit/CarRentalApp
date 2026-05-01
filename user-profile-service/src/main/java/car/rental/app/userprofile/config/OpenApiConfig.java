package car.rental.app.userprofile.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userProfileOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Profile Service API")
                        .version("v1")
                        .description("User data, preferences and rental history — OAuth2/Keycloak, MongoDB"))
                .externalDocs(new ExternalDocumentation()
                        .description("API Gateway")
                        .url("http://api-gateway:8080"));
    }
}
