package ru.romanov.moneytransferservice.controller.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.moneytransferservice.controller.TransactionController;
import ru.romanov.moneytransferservice.mapper.TransactionMapper;
import ru.romanov.moneytransferservice.model.entity.Transaction;
import ru.romanov.moneytransferservice.model.response.TransactionResponse;
import ru.romanov.moneytransferservice.service.TransactionService;

import java.util.UUID;

/**
 * Контроллер для управления транзакциями.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(value = "/api/transactions", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
@AllArgsConstructor
public class DefaultTransactionController implements TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Override
    public ResponseEntity<TransactionResponse> transferMoney(UUID fromAccount, UUID toAccount, double amount) {
        Transaction transaction = transactionService.transferMoney(fromAccount, toAccount, amount);
        log.info("Transfer money of {} from {} to {}", amount, fromAccount, toAccount);
        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @Override
    public ResponseEntity<TransactionResponse> depositMoney(UUID toAccount, double amount) {
        Transaction transaction = transactionService.depositMoney(toAccount, amount);
        log.info("Deposit money of {} to {}", amount, toAccount);
        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @Override
    public ResponseEntity<TransactionResponse> debitMoney(UUID fromAccount, double amount) {
        Transaction transaction = transactionService.debitMoney(fromAccount, amount);
        log.info("Debit money of {} to {}", amount, fromAccount);
        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }
}

