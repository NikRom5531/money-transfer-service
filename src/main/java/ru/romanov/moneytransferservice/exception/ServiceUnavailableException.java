package ru.romanov.moneytransferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое при недоступности сервиса.
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException extends RuntimeException {
    /**
     * Конструктор исключения с заданным сообщением.
     */
    public ServiceUnavailableException() {
        super("Service unavailable");
    }

    /**
     * Конструктор исключения с заданным сообщением и причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause   Причина исключения.
     */
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}