package ru.romanov.moneytransferservice.service;

import ru.romanov.moneytransferservice.model.enums.TypeTransactionEnum;
import ru.romanov.moneytransferservice.exception.AccountNotFoundException;
import ru.romanov.moneytransferservice.exception.CodeNotSupportedException;
import ru.romanov.moneytransferservice.exception.InsufficientFundsException;
import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.Account;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы со счетами пользователей.
 */
public interface AccountService {

    /**
     * Создает новый счёт для пользователя.
     *
     * @param currency Валюта счёта.
     * @param userUid  Уникальный номер пользователя.
     * @return {@link Account} - Созданный счёт.
     * @throws UserNotFoundException     Если пользователь не найден.
     * @throws CodeNotSupportedException Если указанная валюта не поддерживается.
     */
    Account createAccount(String currency,
                          UUID userUid);

    /**
     * Возвращает счёт по его номеру.
     *
     * @param accountUid Номер счёта.
     * @return {@link Account} Найденный счёт.
     * @throws AccountNotFoundException Если счёт не найден.
     */
    Account getAccountByAccountNumber(UUID accountUid);

    /**
     * Обновляет баланс счёта в зависимости от типа транзакции.
     *
     * @param accountUid Номер счёта.
     * @param type       Тип транзакции (дебет или кредит).
     * @param amount     Сумма для обновления баланса.
     */
    void updateAccountBalance(UUID accountUid,
                              TypeTransactionEnum type,
                              double amount);

    /**
     * Удаляет счёт по его номеру.
     *
     * @param accountUid Номер счёта.
     */
    void deleteAccount(UUID accountUid);

    /**
     * Возвращает список всех счетов.
     *
     * @return Список всех счетов.
     */
    List<Account> getAccounts();

    /**
     * Возвращает {@link Map} поддерживаемых валют.
     *
     * @return {@link Map} поддерживаемых валют.
     */
    Map<String, String> getSupportedCurrencyMap();

    /**
     * Проверяет достаточность средств на счёте.
     *
     * @param accountBalance Баланс счёта.
     * @param amount         Сумма для проверки.
     * @throws InsufficientFundsException Если на счёте недостаточно средств.
     */
    void checkBalance(double accountBalance,
                      double amount);
}
