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

    @Override
    public User createUser(String lastName, String firstName, String patronymicName, LocalDate birthDate, String email, String phoneNumber) {
        return userRepository.save(
                User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .patronymicName(patronymicName)
                        .birthDate(birthDate)
                        .email(email)
                        .phoneNumber(phoneNumber)
                        .build());
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUid(UUID uid) {
        return userRepository.findById(uid).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID uid) {
        accountRepository.findByOwnerUid(getUserByUid(uid).getUid()).forEach(account -> accountService.deleteAccount(account.getUid()));
        userRepository.deleteById(uid);
    }
}
