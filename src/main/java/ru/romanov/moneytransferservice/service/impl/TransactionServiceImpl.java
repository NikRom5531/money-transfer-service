package ru.romanov.moneytransferservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.romanov.moneytransferservice.client.CurrencyConverterClient;
import ru.romanov.moneytransferservice.enums.TypeTransactionEnum;
import ru.romanov.moneytransferservice.exception.TransferYourselfException;
import ru.romanov.moneytransferservice.model.entity.Account;
import ru.romanov.moneytransferservice.model.entity.Transaction;
import ru.romanov.moneytransferservice.repository.TransactionRepository;
import ru.romanov.moneytransferservice.service.AccountService;
import ru.romanov.moneytransferservice.service.TransactionService;

import java.time.LocalDateTime;

/**
 * Реализация сервиса для выполнения транзакций между счетами.
 */
@Slf4j
@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private CurrencyConverterClient currencyConverterClient;

    @Override
    public Transaction createTransaction(String fromAccountNumber, String toAccountNumber, TypeTransactionEnum type, double amount, String currencyCode) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now());
        if (fromAccountNumber != null) transaction.setFromAccountNumber(fromAccountNumber);
        if (toAccountNumber != null) transaction.setToAccountNumber(toAccountNumber);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setCurrencyCode(currencyCode);
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction transferMoney(String fromAccountNumber, String toAccountNumber, double amount) {
        if (fromAccountNumber.equals(toAccountNumber)) throw new TransferYourselfException();
        Account fromAccount = accountService.getAccountByAccountNumber(fromAccountNumber);
        Account toAccount = accountService.getAccountByAccountNumber(toAccountNumber);
        double convertedAmount;
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency()))
            convertedAmount = currencyConverterClient.convert(fromAccount.getCurrency(), toAccount.getCurrency(), amount);
        else convertedAmount = amount;
        accountService.updateAccountBalance(fromAccountNumber, TypeTransactionEnum.DEBIT, amount);
        accountService.updateAccountBalance(toAccountNumber, TypeTransactionEnum.DEPOSIT, convertedAmount);
        return createTransaction(fromAccountNumber, toAccountNumber, TypeTransactionEnum.TRANSFER, amount, fromAccount.getCurrency());
    }

    @Override
    @Transactional
    public Transaction depositMoney(String toAccountNumber, double amount) {
        Account toAccount = accountService.getAccountByAccountNumber(toAccountNumber);
        accountService.updateAccountBalance(toAccountNumber, TypeTransactionEnum.DEPOSIT, amount);
        return createTransaction(null, toAccountNumber, TypeTransactionEnum.DEPOSIT, amount, toAccount.getCurrency());
    }

    @Override
    @Transactional
    public Transaction debitMoney(String fromAccountNumber, double amount) {
        Account fromAccount = accountService.getAccountByAccountNumber(fromAccountNumber);
        accountService.updateAccountBalance(fromAccountNumber, TypeTransactionEnum.DEBIT, amount);
        return createTransaction(fromAccountNumber, null, TypeTransactionEnum.DEBIT, amount, fromAccount.getCurrency());
    }
}
