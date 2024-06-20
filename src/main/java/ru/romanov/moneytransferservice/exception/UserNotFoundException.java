package ru.romanov.moneytransferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое при отсутствии пользователя.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    /**
     * Конструктор исключения с заданным сообщением.
     */
    public UserNotFoundException() {
        super("User not found");
    }
}
