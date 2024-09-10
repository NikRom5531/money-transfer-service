package ru.romanov.moneytransferservice.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}

