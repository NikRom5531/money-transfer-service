package ru.romanov.moneytransferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.moneytransferservice.model.entity.Transaction;

import java.util.List;

/**
 * Репозиторий для работы с сущностью Transaction, предоставляющий методы для доступа к базе данных.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Находит все транзакции, где указанный счет является отправителем или получателем.
     *
     * @param fromAccountNumber Номер счета отправителя
     * @param toAccountNumber   Номер счета получателя
     * @return Список транзакций, соответствующих критериям
     */
    List<Transaction> findByFromAccountNumberOrToAccountNumber(String fromAccountNumber, String toAccountNumber);

    /**
     * Находит все транзакции, где указанный счет является отправителем.
     *
     * @param fromAccountNumber Номер счета отправителя
     * @return Список транзакций, где указанный счет является отправителем
     */
    List<Transaction> findByFromAccountNumber(String fromAccountNumber);

    /**
     * Находит все транзакции, где указанный счет является получателем.
     *
     * @param toAccountNumber Номер счета получателя
     * @return Список транзакций, где указанный счет является получателем
     */
    List<Transaction> findByToAccountNumber(String toAccountNumber);

    /**
     * Находит все транзакции с указанным кодом валюты.
     *
     * @param currencyCode Код валюты
     * @return Список транзакций с указанным кодом валюты
     */
    List<Transaction> findByCurrencyCode(String currencyCode);
}

