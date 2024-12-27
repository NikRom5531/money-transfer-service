package ru.romanov.moneytransferservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация Swagger для документирования API.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Создает и настраивает экземпляр {@link GroupedOpenApi} для публичного API.
     *
     * @return Настроенный {@link GroupedOpenApi}.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }

    /**
     * Добавляет конфигурацию OpenAPI для установки HTTPS сервера.
     *
     * @return Настроенный {@link OpenAPI}.
     */
    @Bean
    public OpenAPI customOpenAPI(
            @Value("${ngrok.tunnel-url}") String serverUrl
    ) {
        Server prod = new Server();
        prod.setUrl(serverUrl);
        prod.setDescription("Production Server");

        Server dev = new Server();
        dev.setUrl("https://localhost:8081");
        dev.setDescription("Dev Server");

        return new OpenAPI().info(new Info().title("Application API")
                        .version("v1")
                        .contact(new Contact().name("Nikolay Romanov"))
                )
                .servers(List.of(prod, dev));
    }
}
