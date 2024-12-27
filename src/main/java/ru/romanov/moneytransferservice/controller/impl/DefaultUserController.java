package ru.romanov.moneytransferservice.controller.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.moneytransferservice.controller.UserController;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.model.request.UpdateUserRequest;
import ru.romanov.moneytransferservice.service.UserService;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления пользователями.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class DefaultUserController implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.status(users.isEmpty() ? 204 : 200).body(users);
    }

    @Override
    public ResponseEntity<User> updateUser(UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(request));
    }

    @Override
    public ResponseEntity<User> getUser(UUID uid) {
        return ResponseEntity.ok(userService.getUserByUid(uid));
    }

    @Override
    public ResponseEntity<String> deleteUser(UUID uid) {
        userService.deleteUser(uid);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<String> deleteCurrentUser() {
        userService.deleteYourselfUser();
        return ResponseEntity.noContent().build();
    }
}