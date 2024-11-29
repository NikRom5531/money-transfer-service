package ru.romanov.moneytransferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое при неподдерживаемом коде валюты.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CodeNotSupportedException extends RuntimeException {

    /**
     * Конструктор исключения с сообщением "Currency code not supported".
     */
    public CodeNotSupportedException() {
        super("Currency code not supported");
    }
}
