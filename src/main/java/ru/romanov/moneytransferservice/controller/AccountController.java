package ru.romanov.moneytransferservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.moneytransferservice.exception.AccountNotFoundException;
import ru.romanov.moneytransferservice.exception.CodeNotSupportedException;
import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.Account;
import ru.romanov.moneytransferservice.service.AccountService;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления счетами.
 */
@Slf4j
@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;

    /**
     * Создает новый счёт.
     *
     * @param currency          Код валюты.
     * @param ownerUniqueNumber Уникальный номер владельца.
     * @return {@link ResponseEntity} с созданным счётом или кодом ошибки.
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestParam String currency, @RequestParam String ownerUniqueNumber) {
        try {
            Account account = accountService.createAccount(currency, ownerUniqueNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (CodeNotSupportedException | UserNotFoundException e) {
            log.error("[400 BAD REQUEST] AccountController.createAccount() / message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Возвращает список всех счетов.
     *
     * @return {@link ResponseEntity} со списком счетов или кодом ошибки.
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = accountService.getAccounts();
        if (accounts.isEmpty()) {
            log.warn("[204 NO CONTENT] AccountController.getAccounts()");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(accounts);
    }

    /**
     * Возвращает карту поддерживаемых валют.
     *
     * @return {@link ResponseEntity} с картой поддерживаемых валют.
     */
    @GetMapping("/supported-currency-map")
    public ResponseEntity<Map<String, String>> getSupportedCurrencyMap() {
        return ResponseEntity.ok(accountService.getSupportedCurrencyMap());
    }

    /**
     * Возвращает информацию о счёте по его номеру.
     *
     * @param account_number Номер счёта.
     * @return {@link ResponseEntity} с информацией о счёте или кодом ошибки.
     */
    @GetMapping("/{account_number}")
    public ResponseEntity<Account> getAccount(@PathVariable String account_number) {
        try {
            return ResponseEntity.ok(accountService.getAccountByAccountNumber(account_number));
        } catch (AccountNotFoundException e) {
            log.error("[404 NOT FOUND] AccountController.getAccount() / message: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Удаляет счёт по его номеру.
     *
     * @param account_number Номер счёта.
     * @return {@link ResponseEntity} с сообщением об успешном удалении или кодом ошибки.
     */
    @DeleteMapping("/{account_number}")
    public ResponseEntity<String> deleteAccount(@PathVariable String account_number) {
        try {
            accountService.deleteAccount(account_number);
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (AccountNotFoundException e) {
            log.error("[404 NOT FOUND] AccountController.deleteAccount() / message: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

