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

    /**
     * Создает новый счёт для пользователя.
     *
     * @param currency                      Валюта счёта
     * @param userUniqueNumber              Уникальный номер пользователя
     * @return                              Созданный счёт
     * @throws UserNotFoundException        если пользователь не найден
     * @throws CodeNotSupportedException    если указанная валюта не поддерживается
     */
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

    /**
     * Возвращает счёт по его номеру.
     *
     * @param accountNumber             Номер счёта
     * @return                          Найденный счёт
     * @throws AccountNotFoundException если счёт не найден
     */
    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
    }

    /**
     * Обновляет баланс счёта в зависимости от типа транзакции.
     *
     * @param accountNumber Номер счёта
     * @param type          Тип транзакции (дебет или кредит)
     * @param balance       Сумма для обновления баланса
     */
    @Override
    public void updateAccountBalance(String accountNumber, TypeTransactionEnum type, double balance) {
        Account account = getAccountByAccountNumber(accountNumber);
        double accountBalance = account.getBalance();
        switch (type) {
            case DEPOSIT -> account.setBalance(accountBalance + balance);
            case DEBIT -> {
                checkBalance(accountNumber, balance);
                account.setBalance(accountBalance - balance);
            }
        }
        accountRepository.save(account);
    }

    /**
     * Удаляет счёт по его номеру.
     *
     * @param accountNumber Номер счёта
     */
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

    /**
     * Возвращает список всех счетов.
     *
     * @return Список всех счетов
     */
    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Возвращает карту поддерживаемых валют.
     *
     * @return Карта поддерживаемых валют
     */
    @Override
    public Map<String, String> getSupportedCurrencyMap() {
        return currencyConverterClient.supportedCurrencyMap();
    }

    /**
     * Проверка: поддерживается ли указанный код валюты.
     *
     * @param code Код валюты
     * @return Код валюты в верхнем регистре
     * @throws CodeNotSupportedException если указанный код валюты не поддерживается
     */
    private String checkSupportedCode(String code) {
        if (!getSupportedCurrencyMap().containsKey(code.toUpperCase())) throw new CodeNotSupportedException();
        return code.toUpperCase();
    }

    /**
     * Генерирует уникальный номер счёта.
     *
     * @return Уникальный номер счёта
     */
    private String generateUniqueNumber() {
        int blocks = 4;
        int lengthBlock = 6;
        StringBuilder accountNumber;
        do {
            accountNumber = new StringBuilder();
            for (int i = 0; i < blocks; i++)
                accountNumber.append(UUID.randomUUID()
                                .toString()
                                .toUpperCase()
                                .replace("-", "/"), 0, lengthBlock)
                        .append((i == blocks - 1 ? "" : "-"));
        } while (accountRepository.existsByAccountNumber(accountNumber.toString()));
        return accountNumber.toString();
    }

    /**
     * Проверяет достаточность средств на счёте.
     *
     * @param accountNumber Номер счёта
     * @param amount        Сумма для проверки
     * @throws InsufficientFundsException если на счёте недостаточно средств
     */
    public void checkBalance(String accountNumber, double amount) {
        if (getAccountByAccountNumber(accountNumber).getBalance() - amount < 0)
            throw new InsufficientFundsException();
    }
}

