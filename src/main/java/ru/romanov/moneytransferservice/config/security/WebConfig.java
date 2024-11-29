/*package ru.romanov.moneytransferservice.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") // разрешить все пути
                .allowedOrigins("*") // разрешить запросы с любых источников
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
//                .allowCredentials(true) // Если используете куки или авторизацию
        ;
    }
}*/