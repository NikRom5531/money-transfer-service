package ru.romanov.moneytransferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое при неподдерживаемом коде.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CodeNotSupportedException extends RuntimeException {
    /**
     * Конструктор исключения с сообщением "Code not supported".
     */
    public CodeNotSupportedException() {
        super("Code not supported");
    }
}
