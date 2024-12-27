package ru.romanov.moneytransferservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.romanov.moneytransferservice.exception.UserAlreadyExistsException;
import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.model.request.CreateUserRequest;
import ru.romanov.moneytransferservice.model.request.UpdateUserRequest;
import ru.romanov.moneytransferservice.repository.AccountRepository;
import ru.romanov.moneytransferservice.repository.UserRepository;
import ru.romanov.moneytransferservice.service.AccountService;
import ru.romanov.moneytransferservice.service.SecurityService;
import ru.romanov.moneytransferservice.service.UserService;

import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса для работы с пользователями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final SecurityService securityService;

    @Override
    public User createUser(CreateUserRequest request) {
        checkEmail(request.getEmail());
        checkPhoneNumber(request.getPhoneNumber());

        return userRepository.save(
                User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .patronymicName(request.getPatronymicName())
                        .birthDate(request.getBirthDate())
                        .email(request.getEmail())
                        .phoneNumber(request.getPhoneNumber())
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
    public User updateUser(UpdateUserRequest request) {
        User user = request.getUid() == null ? securityService.getCurrentUser() : getUserByUid(request.getUid());

        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            user.setLastName(request.getLastName());
        }

        if (request.getPatronymicName() != null && !request.getPatronymicName().isEmpty()) {
            user.setPatronymicName(request.getPatronymicName());
        }

        if (request.getBirthDate() != null) {
            user.setBirthDate(request.getBirthDate());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            checkPhoneNumber(request.getPhoneNumber());
            user.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            checkEmail(request.getEmail());
            user.setEmail(request.getEmail());
        }

        log.info("User updated: {}", user);
        return userRepository.save(user);
    }

    @Override
    public void deleteYourselfUser() {
        deleteUser(securityService.getCurrentUser().getUid());
    }

    @Override
    public void deleteUser(UUID uid) {
        accountRepository.findByOwnerUid(getUserByUid(uid).getUid())
                .forEach(account -> accountService.deleteAccount(account.getUid()));
        userRepository.deleteById(uid);
        log.info("User deleted: {}", uid);
    }

    private void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email + " already registered");
        }
    }

    private void checkPhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserAlreadyExistsException(phoneNumber + " already registered");
        }
    }
}
