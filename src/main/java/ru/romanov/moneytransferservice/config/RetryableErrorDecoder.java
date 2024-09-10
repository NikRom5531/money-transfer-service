package ru.romanov.moneytransferservice.config;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

/**
 * Класс для декодирования ошибок Feign клиента с возможностью повторных попыток при возникновении определенных ошибок.
 */
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
        if (response.status() == 429 || response.status() >= 500) {
            System.out.println(response.status());
            return new RetryableException(
                    response.status(),
                    "Retrying due to server error",
                    response.request().httpMethod(),
                    null,
                    response.request()
            );
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
