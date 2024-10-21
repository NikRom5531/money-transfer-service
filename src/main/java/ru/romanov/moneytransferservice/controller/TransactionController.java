package ru.romanov.moneytransferservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.moneytransferservice.exception.AccountNotFoundException;
import ru.romanov.moneytransferservice.model.entity.Transaction;
import ru.romanov.moneytransferservice.service.TransactionService;

import java.util.UUID;

/**
 * Контроллер для управления транзакциями.
 */
@RestController
@RequestMapping(value = "/api/transactions", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;

    /**
     * Выполняет перевод денег между счетами.
     *
     * @param fromAccount Номер счёта, с которого производится перевод.
     * @param toAccount   Номер счёта, на который производится перевод.
     * @param amount      Сумма перевода.
     * @return {@link ResponseEntity} с созданной транзакцией или кодом ошибки.
     */
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferMoney(@RequestParam UUID fromAccount,
                                                     @RequestParam UUID toAccount,
                                                     @RequestParam double amount) {
        if ((fromAccount == null && toAccount == null) || amount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            if (fromAccount != null && fromAccount.equals(toAccount)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                try {
                    return ResponseEntity.ok(transactionService.transferMoney(fromAccount, toAccount, amount));
                } catch (AccountNotFoundException e) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Error-Message", "Account Not Found").build();
                }
            }
        }
    }

    /**
     * Выполняет зачисление денег на счёт.
     *
     * @param toAccount Номер счёта, на который производится зачисление.
     * @param amount    Сумма зачисления.
     * @return {@link ResponseEntity} с созданной транзакцией.
     */
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> depositMoney(@RequestParam UUID toAccount,
                                                    @RequestParam double amount) {
        return ResponseEntity.ok(transactionService.depositMoney(toAccount, amount));
    }

    /**
     * Выполняет списание денег со счёта.
     *
     * @param fromAccount Номер счёта, с которого производится списание.
     * @param amount      Сумма списания.
     * @return {@link ResponseEntity} с созданной транзакцией.
     */
    @PostMapping("/debit")
    public ResponseEntity<Transaction> debitMoney(@RequestParam UUID fromAccount,
                                                  @RequestParam double amount) {
        return ResponseEntity.ok(transactionService.debitMoney(fromAccount, amount));
    }
}

