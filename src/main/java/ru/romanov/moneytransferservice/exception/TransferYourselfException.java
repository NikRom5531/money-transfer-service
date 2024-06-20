package ru.romanov.moneytransferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое при попытке выполнить перевод на собственный счёт.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class TransferYourselfException extends RuntimeException {
    /**
     * Конструктор исключения с заданным сообщением.
     */
    public TransferYourselfException() {
        super("You cannot transfer to yourself");
    }
}
