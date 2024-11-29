package ru.romanov.moneytransferservice.config;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Класс для декодирования ошибок Feign клиента с возможностью повторных попыток при возникновении определенных ошибок.
 */
@Slf4j
public class RetryableErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    /**
     * Метод для декодирования ошибки, возвращаемой сервером.
     * Если статус ответа равен 429 (Too Many Requests) или >= 500 (ошибки сервера),
     * генерируется {@link RetryableException} для повторной попытки запроса.
     * В противном случае используется стандартный декодер ошибок.
     *
     * @param methodKey Ключ метода Feign клиента.
     * @param response  Ответ сервера.
     * @return Исключение, которое будет выброшено.
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 429) {
            System.out.println(response.status());
            log.error("[{}] Retrying due to server error", response.status());
            return new RetryableException(
                    response.status(),
                    "Retrying due to server error",
                    response.request().httpMethod(),
                    Date.valueOf(LocalDate.now().plusDays(1)),
                    response.request()
            );
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
