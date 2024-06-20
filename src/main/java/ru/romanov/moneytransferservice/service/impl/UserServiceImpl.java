package ru.romanov.moneytransferservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.repository.AccountRepository;
import ru.romanov.moneytransferservice.repository.UserRepository;
import ru.romanov.moneytransferservice.service.AccountService;
import ru.romanov.moneytransferservice.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса для работы с пользователями.
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private AccountService accountService;

    /**
     * Создает нового пользователя.
     *
     * @param lastName       Фамилия
     * @param firstName      Имя
     * @param patronymicName Отчество
     * @param birthDate      Дата рождения
     * @param email          Электронная почта
     * @param phoneNumber    Номер телефона
     * @return Созданный пользователь
     */
    @Override
    public User createUser(String lastName, String firstName, String patronymicName, LocalDate birthDate, String email, String phoneNumber) {
        User user = new User();
        user.setUniqueNumber(generateUniqueNumber());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPatronymicName(patronymicName);
        user.setBirthDate(birthDate);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        return userRepository.save(user);
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return Список всех пользователей
     */
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя
     * @return Найденный пользователь
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Возвращает пользователя по его уникальному номеру.
     *
     * @param uniqueNumber Уникальный номер пользователя
     * @return Найденный пользователь
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public User getUserByUniqueNumber(String uniqueNumber) {
        return userRepository.findByUniqueNumber(uniqueNumber).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Обновляет информацию о пользователе.
     *
     * @param user Пользователь для обновления
     */
    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    /**
     * Удаляет пользователя по его идентификатору, а также все его связанные аккаунты.
     *
     * @param id Идентификатор пользователя для удаления
     */
    @Override
    public void deleteUser(long id) {
        accountRepository.findByOwnerUniqueNumber(getUserById(id).getUniqueNumber()).forEach(account -> accountService.deleteAccount(account.getAccountNumber()));
        userRepository.deleteById(id);
    }

    /**
     * Генерирует уникальный номер пользователя.
     *
     * @return Уникальный номер пользователя
     */
    private String generateUniqueNumber() {
        int blocks = 3;
        int lengthBlock = 8;
        StringBuilder uniqueNumber;
        do {
            uniqueNumber = new StringBuilder();
            for (int i = 0; i < blocks; i++) {
                uniqueNumber.append(UUID.randomUUID().toString().toUpperCase().replace("-", "/"), 0, lengthBlock).append((i == blocks - 1 ? "" : "-"));
            }
        } while (userRepository.existsByUniqueNumber(uniqueNumber.toString()));
        return uniqueNumber.toString();
    }
}
