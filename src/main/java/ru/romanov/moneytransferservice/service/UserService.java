package ru.romanov.moneytransferservice.service;

import ru.romanov.moneytransferservice.model.entity.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс сервиса для работы с пользователями.
 */
public interface UserService {
    User createUser(String lastName, String firstName, String patronymicName, LocalDate birthDate, String email, String phoneNumber);

    List<User> getUsers();

    User getUserById(long id);

    User getUserByUniqueNumber(String uniqueNumber);

    void updateUser(User user);

    void deleteUser(long id);
}
