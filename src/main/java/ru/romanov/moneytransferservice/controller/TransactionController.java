package ru.romanov.moneytransferservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.moneytransferservice.model.entity.Transaction;
import ru.romanov.moneytransferservice.service.TransactionService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> transferMoney(@RequestParam String fromAccount,
                                                     @RequestParam String toAccount,
                                                     @RequestParam BigDecimal amount) {
        Transaction transaction = transactionService.transferMoney(fromAccount, toAccount, amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }
}

