package ru.romanov.moneytransferservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.romanov.moneytransferservice.client.CurrencyConverterClient;
import ru.romanov.moneytransferservice.model.enums.TypeTransactionEnum;
import ru.romanov.moneytransferservice.exception.AccountNotFoundException;
import ru.romanov.moneytransferservice.exception.CodeNotSupportedException;
import ru.romanov.moneytransferservice.exception.InsufficientFundsException;
import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.Account;
import ru.romanov.moneytransferservice.model.entity.Transaction;
import ru.romanov.moneytransferservice.repository.AccountRepository;
import ru.romanov.moneytransferservice.repository.TransactionRepository;
import ru.romanov.moneytransferservice.repository.UserRepository;
import ru.romanov.moneytransferservice.service.AccountService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Реализация сервиса для работы со счетами пользователей.
 */
@Slf4j
@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CurrencyConverterClient currencyConverterClient;

    @Override
    public Account createAccount(String currency, UUID userUid) {
        return accountRepository.save(
                Account.builder()
                        .currency(checkSupportedCode(currency))
                        .owner(userRepository.findByUid(userUid).orElseThrow(UserNotFoundException::new))
                        .balance(0)
                        .build());
    }

    @Override
    public Account getAccountByAccountNumber(UUID accountUid) {
        return accountRepository.findByUid(accountUid).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public void updateAccountBalance(UUID accountUid, TypeTransactionEnum type, double amount) {
        Account account = getAccountByAccountNumber(accountUid);
        double accountBalance = account.getBalance();
        switch (type) {
            case DEPOSIT -> account.setBalance(accountBalance + amount);
            case DEBIT -> {
                checkBalance(account.getBalance(), amount);
                account.setBalance(accountBalance - amount);
            }
        }
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteAccount(UUID accountUid) {
        Account account = getAccountByAccountNumber(accountUid);
        if (account.getBalance() > 0) {
            double accountBalance = account.getBalance();
            updateAccountBalance(accountUid, TypeTransactionEnum.DEBIT, accountBalance);
            transactionRepository.save(
                    Transaction.builder()
                            .transactionDate(LocalDateTime.now())
                            .fromAccount(account)
                            .amount(accountBalance)
                            .type(TypeTransactionEnum.DEBIT)
                            .currencyCode(account.getCurrency())
                            .build());
        }
        accountRepository.deleteById(account.getUid());
        log.info("Delete account. Account number: {}", account.getUid());
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Map<String, String> getSupportedCurrencyMap() {
        return currencyConverterClient.supportedCurrencyMap();
    }

    @Override
    public void checkBalance(double accountBalance, double amount) {
        if (accountBalance - amount < 0)
            throw new InsufficientFundsException();
    }

    /**
     * Проверка: поддерживается ли указанный код валюты.
     *
     * @param code Код валюты.
     * @return Код валюты в верхнем регистре.
     * @throws CodeNotSupportedException Если указанный код валюты не поддерживается.
     */
    private String checkSupportedCode(String code) {
        if (!getSupportedCurrencyMap().containsKey(code.toUpperCase())) throw new CodeNotSupportedException();
        return code.toUpperCase();
    }
}