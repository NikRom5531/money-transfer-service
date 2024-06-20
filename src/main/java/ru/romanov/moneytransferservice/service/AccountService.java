package ru.romanov.moneytransferservice.service;

import ru.romanov.moneytransferservice.enums.TypeTransactionEnum;
import ru.romanov.moneytransferservice.model.entity.Account;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс сервиса для работы со счетами пользователей.
 */
public interface AccountService {
    Account createAccount(String currency, String userIdNumber);

    Account getAccountByAccountNumber(String accountNumber);

    void updateAccountBalance(String accountNumber, TypeTransactionEnum type, double balance);

    Map<String, String> getSupportedCurrencyMap();

    List<Account> getAccounts();

    void checkBalance(String accountNumber, double amount);

    void deleteAccount(String accountNumber);
}
