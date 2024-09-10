package ru.romanov.moneytransferservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.romanov.moneytransferservice.client.CurrencyConverterClient;
import ru.romanov.moneytransferservice.enums.TypeTransactionEnum;
import ru.romanov.moneytransferservice.exception.TransferYourselfException;
import ru.romanov.moneytransferservice.model.entity.Account;
import ru.romanov.moneytransferservice.model.entity.Transaction;
import ru.romanov.moneytransferservice.repository.TransactionRepository;
import ru.romanov.moneytransferservice.service.AccountService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private CurrencyConverterClient currencyConverterClient;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_Success() {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.createTransaction("123", "456", TypeTransactionEnum.TRANSFER, 100.0, "USD");

        assertNotNull(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void transferMoney_Success() {
        String fromAccountNumber = "123";
        String toAccountNumber = "456";
        double amount = 100.0;

        Account fromAccount = new Account();
        fromAccount.setAccountNumber(fromAccountNumber);
        fromAccount.setCurrency("USD");

        Account toAccount = new Account();
        toAccount.setAccountNumber(toAccountNumber);
        toAccount.setCurrency("EUR");

        when(accountService.getAccountByAccountNumber(fromAccountNumber)).thenReturn(fromAccount);
        when(accountService.getAccountByAccountNumber(toAccountNumber)).thenReturn(toAccount);
        when(currencyConverterClient.convert("USD", "EUR", amount)).thenReturn(85.0);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        Transaction result = transactionService.transferMoney(fromAccountNumber, toAccountNumber, amount);

        assertNotNull(result);
        verify(accountService, times(1)).updateAccountBalance(fromAccountNumber, TypeTransactionEnum.DEBIT, amount);
        verify(accountService, times(1)).updateAccountBalance(toAccountNumber, TypeTransactionEnum.DEPOSIT, 85.0);
    }

    @Test
    void transferMoney_TransferYourselfException() {
        String accountNumber = "123";

        assertThrows(TransferYourselfException.class, () -> transactionService.transferMoney(accountNumber, accountNumber, 100.0));
    }

    @Test
    void depositMoney_Success() {
        String toAccountNumber = "456";
        double amount = 100.0;

        Account toAccount = new Account();
        toAccount.setAccountNumber(toAccountNumber);
        toAccount.setCurrency("USD");

        when(accountService.getAccountByAccountNumber(toAccountNumber)).thenReturn(toAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        Transaction result = transactionService.depositMoney(toAccountNumber, amount);

        assertNotNull(result);
        verify(accountService, times(1)).updateAccountBalance(toAccountNumber, TypeTransactionEnum.DEPOSIT, amount);
    }

    @Test
    void debitMoney_Success() {
        String fromAccountNumber = "123";
        double amount = 100.0;

        Account fromAccount = new Account();
        fromAccount.setAccountNumber(fromAccountNumber);
        fromAccount.setCurrency("USD");

        when(accountService.getAccountByAccountNumber(fromAccountNumber)).thenReturn(fromAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        Transaction result = transactionService.debitMoney(fromAccountNumber, amount);

        assertNotNull(result);
        verify(accountService, times(1)).updateAccountBalance(fromAccountNumber, TypeTransactionEnum.DEBIT, amount);
    }
}
