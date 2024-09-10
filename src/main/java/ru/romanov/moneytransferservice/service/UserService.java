package ru.romanov.moneytransferservice.service;

import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс сервиса для работы с пользователями.
 */
public interface UserService {
    /**
     * Создает нового пользователя.
     *
     * @param lastName       Фамилия.
     * @param firstName      Имя.
     * @param patronymicName Отчество.
     * @param birthDate      Дата рождения.
     * @param email          Электронная почта.
     * @param phoneNumber    Номер телефона.
     * @return Созданный пользователь.
     */
    User createUser(String lastName,
                    String firstName,
                    String patronymicName,
                    LocalDate birthDate,
                    String email,
                    String phoneNumber);

    /**
     * Возвращает список всех пользователей.
     *
     * @return Список всех пользователей.
     */
    List<User> getUsers();

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Найденный пользователь.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    User getUserById(long id);

    /**
     * Возвращает пользователя по его уникальному номеру.
     *
     * @param uniqueNumber Уникальный номер пользователя.
     * @return Найденный пользователь.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    User getUserByUniqueNumber(String uniqueNumber);

    /**
     * Обновляет информацию о пользователе.
     *
     * @param user Пользователь для обновления.
     */
    void updateUser(User user);

    /**
     * Удаляет пользователя по его идентификатору, а также все его связанные аккаунты.
     *
     * @param id Идентификатор пользователя для удаления.
     */
    void deleteUser(long id);
}
