package ru.romanov.moneytransferservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.romanov.moneytransferservice.client.CurrencyConverterClient;
import ru.romanov.moneytransferservice.enums.TypeTransactionEnum;
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
    public Account createAccount(String currency, String userUniqueNumber) {
        Account account = new Account();
        account.setCurrency(checkSupportedCode(currency));
        account.setOwnerUniqueNumber(userRepository.findByUniqueNumber(userUniqueNumber).orElseThrow(UserNotFoundException::new).getUniqueNumber());
        account.setAccountNumber(generateUniqueNumber());
        account.setBalance(0);
        log.info("Account created. Account number: {}, currency: {}, owner unique number: {}", account.getAccountNumber(), account.getCurrency(), account.getOwnerUniqueNumber());
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public void updateAccountBalance(String accountNumber, TypeTransactionEnum type, double amount) {
        Account account = getAccountByAccountNumber(accountNumber);
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
    public void deleteAccount(String accountNumber) {
        Account account = getAccountByAccountNumber(accountNumber);
        if (account.getBalance() > 0) {
            double accountBalance = account.getBalance();
            updateAccountBalance(accountNumber, TypeTransactionEnum.DEBIT, accountBalance);
            Transaction transaction = new Transaction();
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setFromAccountNumber(accountNumber);
            transaction.setAmount(accountBalance);
            transaction.setType(TypeTransactionEnum.DEBIT);
            transaction.setCurrencyCode(account.getCurrency());
            transactionRepository.save(transaction);
        }
        accountRepository.deleteById(account.getId());
        log.info("Delete account. Account number: {}", account.getAccountNumber());
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

    /**
     * Генерирует уникальный номер счёта.
     *
     * @return Уникальный номер счёта.
     */
    private String generateUniqueNumber() {
        int blocks = 4;
        int lengthBlock = 6;
        StringBuilder accountNumber;
        do {
            accountNumber = new StringBuilder();
            for (int i = 0; i < blocks; i++)
                accountNumber.append(UUID.randomUUID().toString().toUpperCase()
                                .replace("-", "/"), 0, lengthBlock)
                        .append((i == blocks - 1 ? "" : "-"));
        } while (accountRepository.existsByAccountNumber(accountNumber.toString()));
        return accountNumber.toString();
    }
}