package homework.serviceuser.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@AllArgsConstructor
public class OpenAPIConfiguration {
    private Environment environment;

    @Bean
    public OpenAPI getOpenAPI() {
        Server server = new Server();
        String serverUrl = environment.getProperty("api.server.url");
        server.setUrl(serverUrl);
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Равиль Сафаргулов");
        myContact.setEmail("simpleEmail@example.com");

        Info info = new Info()
                .title("Системное API для управления пользователями")
                .version("1.0")
                .description("Этот API предоставляет эндпоинты для управления пользователями.")
                .contact(myContact);
        return new OpenAPI().info(info).servers(List.of(server));
    }
}
