package ru.romanov.moneytransferservice.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.moneytransferservice.controller.AccountController;
import ru.romanov.moneytransferservice.mapper.AccountMapper;
import ru.romanov.moneytransferservice.model.entity.Account;
import ru.romanov.moneytransferservice.model.response.AccountResponse;
import ru.romanov.moneytransferservice.service.AccountService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для управления счетами.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class DefaultAccountController implements AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<AccountResponse> createAccount(String currency) {
        Account account = accountService.createAccount(currency);
        AccountResponse accountResponse = accountMapper.toResponse(account);
        log.info("Account created: {}", account.getUid());
        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }

    @Override
    public ResponseEntity<List<AccountResponse>> getAccounts() {
        List<AccountResponse> accounts = accountService.getAccounts().stream().map(accountMapper::toResponse).toList();
        return ResponseEntity.status(accounts.isEmpty() ? 204 : 200).body(accounts);
    }

    @Override
    public ResponseEntity<AccountResponse> getAccount(UUID uid) {
        Account account = accountService.getAccountByAccountNumber(uid);
        return ResponseEntity.ok(accountMapper.toResponse(account));
    }

    @Override
    public ResponseEntity<List<AccountResponse>> getAccountsByUserUid() {
        var accounts = accountService.getAccountsByUserUid().stream().map(accountMapper::toResponse).toList();
        return ResponseEntity.status(accounts.isEmpty() ? 204 : 200).body(accounts);
    }

    @Override
    public ResponseEntity<Map<String, String>> getSupportedCurrencyMap() {
        return ResponseEntity.ok(accountService.getSupportedCurrencyMap());
    }

    @Override
    public ResponseEntity<String> deleteAccount(UUID uid) {
        accountService.deleteYourselfAccount(uid);
        log.info("Account deleted: {}", uid);
        return ResponseEntity.noContent().build();
    }
}

