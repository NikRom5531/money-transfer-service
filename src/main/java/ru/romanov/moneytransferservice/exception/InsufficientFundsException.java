package ru.romanov.moneytransferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое при недостаточных средствах на счете.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientFundsException extends RuntimeException {
    /**
     * Конструктор исключения с сообщением "Insufficient funds".
     */
    public InsufficientFundsException() {
        super("Insufficient funds");
    }
}
