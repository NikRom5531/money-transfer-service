package ru.romanov.moneytransferservice.service;

import ru.romanov.moneytransferservice.model.enums.TypeTransactionEnum;
import ru.romanov.moneytransferservice.exception.TransferYourselfException;
import ru.romanov.moneytransferservice.model.entity.Transaction;

import java.util.UUID;

/**
 * Интерфейс сервиса для выполнения транзакций между счетами.
 */
public interface TransactionService {
    /**
     * Создает новую транзакцию.
     *
     * @param fromAccountNumber Номер счёта отправителя (может быть {@code null}).
     * @param toAccountNumber   Номер счёта получателя (может быть {@code null}).
     * @param type              Тип транзакции ({@code TRANSFER}, {@code DEPOSIT}, {@code DEBIT}).
     * @param amount            Сумма транзакции.
     * @param currencyCode      Код валюты.
     * @return Созданная транзакция.
     */
    Transaction createTransaction(UUID fromAccountNumber,
                                  UUID toAccountNumber,
                                  TypeTransactionEnum type,
                                  double amount,
                                  String currencyCode);

    /**
     * Выполняет операцию перевода денег между счетами.
     *
     * @param fromAccountNumber Номер счёта отправителя.
     * @param toAccountNumber   Номер счёта получателя.
     * @param amount            Сумма перевода.
     * @return Созданная транзакция.
     * @throws TransferYourselfException При попытке перевода на счёт отправителя.
     */
    Transaction transferMoney(UUID fromAccountNumber,
                              UUID toAccountNumber,
                              double amount);

    /**
     * Выполняет операцию внесения денег на счёт.
     *
     * @param toAccountNumber Номер счёта.
     * @param amount          Сумма внесения.
     * @return Созданная транзакция.
     */
    Transaction depositMoney(UUID toAccountNumber,
                             double amount);

    /**
     * Выполняет операцию списания денег со счёта.
     *
     * @param fromAccountNumber Номер счёта.
     * @param amount            Сумма списания.
     * @return Созданная транзакция.
     */
    Transaction debitMoney(UUID fromAccountNumber,
                           double amount);
}
