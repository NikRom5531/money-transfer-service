package ru.romanov.moneytransferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.moneytransferservice.model.entity.Account;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Account}, предоставляющий методы для доступа к базе данных.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * Проверяет существование счета с указанным номером.
     *
     * @param accountNumber Номер счета.
     * @return {@code true}, если счет существует, иначе {@code false}.
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Находит счет по его номеру.
     *
     * @param accountNumber Номер счета.
     * @return {@link Optional} с найденным счетом, или {@code Optional.empty()}, если счет не найден.
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Находит все счета, принадлежащие владельцу с указанным уникальным номером.
     *
     * @param ownerUniqueNumber Уникальный номер владельца счетов.
     * @return Список счетов, принадлежащих указанному владельцу.
     */
    List<Account> findByOwnerUniqueNumber(String ownerUniqueNumber);
}

