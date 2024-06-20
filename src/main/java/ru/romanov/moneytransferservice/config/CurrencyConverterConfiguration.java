package ru.romanov.moneytransferservice.config;

import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Collections;

/**
 * Конфигурация для клиента Feign, используемого для взаимодействия с сервисом конвертации валют.
 */
@Configuration
public class CurrencyConverterConfiguration {

    /**
     * Создает и возвращает бин Retryer для настройки повторных попыток в Feign клиенте.
     *
     * @return объект Retryer для настройки повторных попыток
     */
    @Bean
    public Retryer retryer() {
        return new FeignIntervalRetryer();
    }

    /**
     * Создает и возвращает бин ErrorDecoder для обработки ошибок в Feign клиенте.
     *
     * @return объект ErrorDecoder для обработки ошибок
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new RetryableErrorDecoder();
    }

    /**
     * Создает и возвращает бин Decoder для декодирования ответов в Feign клиенте.
     *
     * @param messageConverters фабрика для создания конвертеров HTTP сообщений
     * @return объект Decoder для декодирования ответов
     */
    @Bean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new OptionalDecoder(new SpringDecoder(() -> new HttpMessageConverters(new FeignHttpMessageConverter())));
    }

    /**
     * Внутренний класс, расширяющий MappingJackson2HttpMessageConverter для поддержки дополнительных типов медиа.
     */
    static class FeignHttpMessageConverter extends MappingJackson2HttpMessageConverter {
        /**
         * Конструктор, задающий поддерживаемые типы медиа для конвертера.
         */
        FeignHttpMessageConverter() {
            setSupportedMediaTypes(Collections.singletonList(new org.springframework.http.MediaType("application", "javascript")));
        }
    }
}
