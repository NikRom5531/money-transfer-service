package ru.romanov.moneytransferservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.romanov.moneytransferservice.exception.AccountNotFoundException;
import ru.romanov.moneytransferservice.exception.CodeNotSupportedException;
import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.Account;
import ru.romanov.moneytransferservice.service.AccountService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount_Success() throws CodeNotSupportedException, UserNotFoundException {
        Account account = new Account();
        when(accountService.createAccount(anyString(), anyString())).thenReturn(account);

        ResponseEntity<Account> response = accountController.createAccount("USD", "12345");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(accountService, times(1)).createAccount(anyString(), anyString());
    }

    @Test
    void testCreateAccount_CodeNotSupportedException() throws CodeNotSupportedException, UserNotFoundException {
        when(accountService.createAccount(anyString(), anyString())).thenThrow(new CodeNotSupportedException());

        ResponseEntity<Account> response = accountController.createAccount("XXX", "12345");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(accountService, times(1)).createAccount(anyString(), anyString());
    }

    @Test
    void testCreateAccount_UserNotFoundException() throws CodeNotSupportedException, UserNotFoundException {
        when(accountService.createAccount(anyString(), anyString())).thenThrow(new UserNotFoundException());

        ResponseEntity<Account> response = accountController.createAccount("USD", "99999");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(accountService, times(1)).createAccount(anyString(), anyString());
    }

    @Test
    void testGetAccounts_Success() {
        List<Account> accounts = List.of(new Account(), new Account());
        when(accountService.getAccounts()).thenReturn(accounts);

        ResponseEntity<List<Account>> response = accountController.getAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(accountService, times(1)).getAccounts();
    }

    @Test
    void testGetAccounts_NoContent() {
        when(accountService.getAccounts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Account>> response = accountController.getAccounts();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(accountService, times(1)).getAccounts();
    }

    @Test
    void testGetSupportedCurrencyMap_Success() {
        Map<String, String> currencyMap = Map.of("USD", "United States Dollar", "EUR", "Euro");
        when(accountService.getSupportedCurrencyMap()).thenReturn(currencyMap);

        ResponseEntity<Map<String, String>> response = accountController.getSupportedCurrencyMap();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(accountService, times(1)).getSupportedCurrencyMap();
    }

    @Test
    void testGetAccount_Success() throws AccountNotFoundException {
        Account account = new Account();
        when(accountService.getAccountByAccountNumber(anyString())).thenReturn(account);

        ResponseEntity<Account> response = accountController.getAccount("12345");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(accountService, times(1)).getAccountByAccountNumber(anyString());
    }

    @Test
    void testGetAccount_AccountNotFoundException() throws AccountNotFoundException {
        when(accountService.getAccountByAccountNumber(anyString())).thenThrow(new AccountNotFoundException());

        ResponseEntity<Account> response = accountController.getAccount("99999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(accountService, times(1)).getAccountByAccountNumber(anyString());
    }

    @Test
    void testDeleteAccount_Success() throws AccountNotFoundException {
        ResponseEntity<String> response = accountController.deleteAccount("12345");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(accountService, times(1)).deleteAccount(anyString());
    }

    @Test
    void testDeleteAccount_AccountNotFoundException() throws AccountNotFoundException {
        doThrow(new AccountNotFoundException()).when(accountService).deleteAccount(anyString());

        ResponseEntity<String> response = accountController.deleteAccount("99999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(accountService, times(1)).deleteAccount(anyString());
    }
}
