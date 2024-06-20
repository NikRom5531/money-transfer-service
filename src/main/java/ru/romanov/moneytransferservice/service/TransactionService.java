package ru.romanov.moneytransferservice.service;

import ru.romanov.moneytransferservice.enums.TypeTransactionEnum;
import ru.romanov.moneytransferservice.model.entity.Transaction;

/**
 * Интерфейс сервиса для выполнения транзакций между счетами.
 */
public interface TransactionService {
    Transaction createTransaction(String fromAccountNumber, String toAccountNumber, TypeTransactionEnum type, double amount, String currencyCode);

    Transaction transferMoney(String fromAccountNumber, String toAccountNumber, double amount);

    Transaction depositMoney(String toAccountNumber, double amount);

    Transaction debitMoney(String fromAccount, double amount);
}
