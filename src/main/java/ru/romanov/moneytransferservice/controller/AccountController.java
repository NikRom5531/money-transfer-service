package ru.romanov.moneytransferservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.util.UUID;

/**
 * Контроллер для управления счетами.
 */
@Slf4j
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /**
     * Создает новый счёт.
     *
     * @param currency Код валюты.
     * @param ownerUid Уникальный номер владельца.
     * @return {@link ResponseEntity} с созданным счётом или кодом ошибки.
     */
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Account> createAccount(@RequestParam String currency, @RequestParam UUID ownerUid) {
        try {
            Account account = accountService.createAccount(currency, ownerUid);
            log.info("Account created. Account number: {}, currency: {}, owner uid: {}",
                    account.getUid(), account.getCurrency(), account.getOwner());
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (CodeNotSupportedException e1) {
            log.error("[400 BAD REQUEST] AccountController.createAccount() / message: {}", e1.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (UserNotFoundException e2) {
            log.error("[404 NOT FOUND] AccountController.createAccount() / message: {}", e2.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Возвращает список всех счетов.
     *
     * @return {@link ResponseEntity} со списком счетов или кодом ошибки.
     */
    @GetMapping("/list")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = accountService.getAccounts();
        if (accounts.isEmpty()) {
            log.warn("[204 NO CONTENT] AccountController.getAccounts()");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(accounts);
    }

    /**
     * Возвращает информацию о счёте по его номеру.
     *
     * @param accountNumber Номер счёта.
     * @return {@link ResponseEntity} с информацией о счёте или кодом ошибки.
     */
    @GetMapping
    public ResponseEntity<Account> getAccount(@RequestParam UUID accountNumber) {
        try {
            return ResponseEntity.ok(accountService.getAccountByAccountNumber(accountNumber));
        } catch (AccountNotFoundException e) {
            log.error("[404 NOT FOUND] AccountController.getAccount() / message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
     * Удаляет счёт по его номеру.
     *
     * @param accountNumber Номер счёта.
     * @return {@link ResponseEntity} с сообщением об успешном удалении или кодом ошибки.
     */
    @DeleteMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> deleteAccount(@RequestParam UUID accountNumber) {
        try {
            accountService.deleteAccount(accountNumber);
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (AccountNotFoundException e) {
            log.error("[404 NOT FOUND] AccountController.deleteAccount() / message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

