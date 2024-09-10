package ru.romanov.moneytransferservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.romanov.moneytransferservice.client.CurrencyConverterClient;
import ru.romanov.moneytransferservice.enums.TypeTransactionEnum;
import ru.romanov.moneytransferservice.exception.AccountNotFoundException;
import ru.romanov.moneytransferservice.exception.InsufficientFundsException;
import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.Account;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.repository.AccountRepository;
import ru.romanov.moneytransferservice.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrencyConverterClient currencyConverterClient;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount_UserNotFoundException() {
        String currency = "USD";

        when(userRepository.findByUniqueNumber(anyString())).thenReturn(Optional.empty());
        when(currencyConverterClient.supportedCurrencyMap()).thenReturn(Map.of(currency, "Dollar"));

        assertThrows(UserNotFoundException.class, () -> accountService.createAccount(currency, "12345"));

        verify(userRepository, times(1)).findByUniqueNumber(anyString());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testCreateAccount_Success() {
        String userUniqueNumber = "12345";
        String currency = "USD";

        User user = new User();
        user.setUniqueNumber(userUniqueNumber);

        when(userRepository.findByUniqueNumber(userUniqueNumber)).thenReturn(Optional.of(user));
        when(currencyConverterClient.supportedCurrencyMap()).thenReturn(Map.of(currency, "Dollar"));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account account = accountService.createAccount(currency, userUniqueNumber);

        assertNotNull(account);
        assertEquals(userUniqueNumber, account.getOwnerUniqueNumber());
        assertEquals(currency, account.getCurrency());

        verify(accountRepository, times(1)).save(any(Account.class));
    }


    @Test
    void testGetAccountByAccountNumber_AccountNotFoundException() {
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByAccountNumber("99999"));

        verify(accountRepository, times(1)).findByAccountNumber(anyString());
    }

    @Test
    void testGetAccountByAccountNumber_Success() {
        Account account = new Account();
        account.setAccountNumber("12345");
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        Account result = accountService.getAccountByAccountNumber("12345");

        assertNotNull(result);
        assertEquals("12345", result.getAccountNumber());
        verify(accountRepository, times(1)).findByAccountNumber("12345");
    }

    @Test
    void testUpdateAccountBalance_DebitInsufficientFundsException() {
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(50.0);
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () ->
                accountService.updateAccountBalance("12345", TypeTransactionEnum.DEBIT, 100.0));

        verify(accountRepository, times(1)).findByAccountNumber("12345");
    }

    @Test
    void testUpdateAccountBalance_DepositSuccess() {
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(50.0);
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        accountService.updateAccountBalance("12345", TypeTransactionEnum.DEPOSIT, 50.0);

        verify(accountRepository, times(1)).save(account);
        assertEquals(100.0, account.getBalance());
    }

    @Test
    void testUpdateAccountBalance_DebitSuccess() {
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setBalance(100.0);
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        accountService.updateAccountBalance("12345", TypeTransactionEnum.DEBIT, 50.0);

        verify(accountRepository, times(1)).save(account);
        assertEquals(50.0, account.getBalance());
    }

    @Test
    void testDeleteAccount_AccountNotFoundException() {
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount("99999"));

        verify(accountRepository, times(1)).findByAccountNumber(anyString());
    }

    @Test
    void testDeleteAccount_Success() {
        String accountNumber = "12345";
        long accountId = 1L;

        Account account = new Account();
        account.setId(accountId);
        account.setAccountNumber(accountNumber);
        account.setBalance(0.0);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        accountService.deleteAccount(accountNumber);

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountRepository, times(1)).deleteById(accountId);
    }
}
