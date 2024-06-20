package ru.romanov.moneytransferservice.exception;

/**
 * Исключение, выбрасываемое при недоступности сервиса.
 */
public class ServiceUnavailableException extends RuntimeException {
    /**
     * Конструктор исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public ServiceUnavailableException(String message) {
        super(message);
    }

    /**
     * Конструктор исключения с заданным сообщением и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина исключения
     */
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}