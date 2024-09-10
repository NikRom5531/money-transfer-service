package ru.romanov.moneytransferservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.moneytransferservice.model.entity.Transaction;
import ru.romanov.moneytransferservice.service.TransactionService;

/**
 * Контроллер для управления транзакциями.
 */
@RestController
@RequestMapping("/api/transactions")
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
    public ResponseEntity<Transaction> transferMoney(@RequestParam String fromAccount,
                                                     @RequestParam String toAccount,
                                                     @RequestParam double amount) {
        if ((fromAccount == null && toAccount == null) || amount <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (fromAccount != null && toAccount != null)
            if (fromAccount.equals(toAccount)) return new ResponseEntity<>(HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.transferMoney(fromAccount, toAccount, amount));
    }

    /**
     * Выполняет зачисление денег на счёт.
     *
     * @param toAccount Номер счёта, на который производится зачисление.
     * @param amount    Сумма зачисления.
     * @return {@link ResponseEntity} с созданной транзакцией.
     */
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> depositMoney(@RequestParam String toAccount,
                                                    @RequestParam double amount) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.depositMoney(toAccount, amount));
    }

    /**
     * Выполняет списание денег со счёта.
     *
     * @param fromAccount Номер счёта, с которого производится списание.
     * @param amount      Сумма списания.
     * @return {@link ResponseEntity} с созданной транзакцией.
     */
    @PostMapping("/debit")
    public ResponseEntity<Transaction> debitMoney(@RequestParam String fromAccount,
                                                  @RequestParam double amount) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.debitMoney(fromAccount, amount));
    }
}

