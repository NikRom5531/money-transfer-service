package ru.romanov.moneytransferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.moneytransferservice.model.entity.Transaction;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью {@link Transaction}, предоставляющий методы для доступа к базе данных.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    /**
     * Находит все транзакции, где указанный счет является отправителем или получателем.
     *
     * @param fromAccountUid Номер счета отправителя.
     * @param toAccountUid   Номер счета получателя.
     * @return Список транзакций, соответствующих критериям.
     */
    List<Transaction> findByFromAccountUidOrToAccountUid(UUID fromAccountUid, UUID toAccountUid);

    /**
     * Находит все транзакции, где указанный счет является отправителем.
     *
     * @param fromAccountUid Номер счета отправителя.
     * @return Список транзакций, где указанный счет является отправителем.
     */
    List<Transaction> findByFromAccountUid(UUID fromAccountUid);

    /**
     * Находит все транзакции, где указанный счет является получателем.
     *
     * @param toAccountUid Номер счета получателя.
     * @return Список транзакций, где указанный счет является получателем.
     */
    List<Transaction> findByToAccountUid(UUID toAccountUid);

    /**
     * Находит все транзакции с указанным кодом валюты.
     *
     * @param currencyCode Код валюты.
     * @return Список транзакций с указанным кодом валюты.
     */
    List<Transaction> findByCurrencyCode(String currencyCode);
}

