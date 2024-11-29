package ru.romanov.moneytransferservice.service;

import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.model.request.CreateUserRequest;
import ru.romanov.moneytransferservice.model.request.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с пользователями.
 */
public interface UserService {
    /**
     * Создает нового пользователя.
     *
     * @param request Данные нового пользователя.
     * @return Созданный пользователь.
     */
    User createUser(CreateUserRequest request);

    /**
     * Возвращает список всех пользователей.
     *
     * @return Список всех пользователей.
     */
    List<User> getUsers();

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param uid Идентификатор пользователя.
     * @return Найденный пользователь.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    User getUserByUid(UUID uid);

    /**
     * Обновляет информацию о пользователе.
     *
     * @param request Пользователь для обновления.
     */
    User updateUser(UpdateUserRequest request);

    /**
     * Удаляет пользователя по его идентификатору, а также все его связанные счета.
     *
     * @param uid Идентификатор пользователя для удаления.
     */
    void deleteUser(UUID uid);

    /**
     * Удаляет пользователя, а также все его связанные счета.
     *
     */
    void deleteYourselfUser();
}
