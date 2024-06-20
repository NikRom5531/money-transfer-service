package ru.romanov.moneytransferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.moneytransferservice.model.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User, предоставляющий методы для доступа к базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Проверяет существование пользователя по уникальному номеру.
     *
     * @param userIdNumber Уникальный номер пользователя
     * @return true, если пользователь существует, в противном случае - false
     */
    boolean existsByUniqueNumber(String userIdNumber);

    /**
     * Находит пользователя по уникальному номеру.
     *
     * @param userIdNumber Уникальный номер пользователя
     * @return Optional с найденным пользователем или пустой Optional, если пользователь не найден
     */
    Optional<User> findByUniqueNumber(String userIdNumber);

    /**
     * Находит пользователя по email.
     *
     * @param email Email пользователя
     * @return Optional с найденным пользователем или пустой Optional, если пользователь не найден
     */
    Optional<User> findByEmail(String email);

    /**
     * Находит пользователя по номеру телефона.
     *
     * @param phoneNumber Номер телефона пользователя
     * @return Optional с найденным пользователем или пустой Optional, если пользователь не найден
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * Находит список пользователей по фамилии и имени.
     *
     * @param lastName  Фамилия пользователя
     * @param firstName Имя пользователя
     * @return Список пользователей с указанной фамилией и именем
     */
    List<User> findByLastNameAndFirstName(String lastName, String firstName);

    /**
     * Находит список пользователей по фамилии, имени и отчеству.
     *
     * @param lastName      Фамилия пользователя
     * @param firstName     Имя пользователя
     * @param patronymicName Отчество пользователя
     * @return Список пользователей с указанной фамилией, именем и отчеством
     */
    List<User> findByLastNameAndFirstNameAndPatronymicName(String lastName, String firstName, String patronymicName);
}
