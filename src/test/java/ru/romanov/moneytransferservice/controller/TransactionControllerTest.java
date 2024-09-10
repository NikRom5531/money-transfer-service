package ru.romanov.moneytransferservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.romanov.moneytransferservice.model.entity.Transaction;
import ru.romanov.moneytransferservice.service.TransactionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void transferMoney_Success() {
        Transaction transaction = new Transaction();
        when(transactionService.transferMoney(anyString(), anyString(), anyDouble())).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.transferMoney("123", "456", 100.0);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).transferMoney("123", "456", 100.0);
    }

    @Test
    void transferMoney_BadRequest() {
        ResponseEntity<Transaction> response = transactionController.transferMoney(null, null, 100.0);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(transactionService, times(0)).transferMoney(anyString(), anyString(), anyDouble());
    }

    @Test
    void transferMoney_Conflict() {
        ResponseEntity<Transaction> response = transactionController.transferMoney("123", "123", 100.0);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(transactionService, times(0)).transferMoney(anyString(), anyString(), anyDouble());
    }

    @Test
    void depositMoney_Success() {
        Transaction transaction = new Transaction();
        when(transactionService.depositMoney(anyString(), anyDouble())).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.depositMoney("456", 200.0);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).depositMoney("456", 200.0);
    }

    @Test
    void debitMoney_Success() {
        Transaction transaction = new Transaction();
        when(transactionService.debitMoney(anyString(), anyDouble())).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.debitMoney("123", 150.0);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).debitMoney("123", 150.0);
    }
}
