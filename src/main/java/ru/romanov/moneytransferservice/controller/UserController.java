package ru.romanov.moneytransferservice.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.moneytransferservice.model.dto.UserDto;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.service.UserService;
import ru.romanov.moneytransferservice.service.ValidationService;

import java.util.List;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Validated
public class UserController {
    private UserService userService;
    private ValidationService validationService;

    /**
     * Создает нового пользователя.
     *
     * @param userDTO Данные нового пользователя.
     * @return {@link ResponseEntity} с созданным пользователем или кодом ошибки.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDTO) {
        if (!validationService.isUserDtoValidated(userDTO)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(
                userDTO.getLastName(),
                userDTO.getFirstName(),
                userDTO.getPatronymicName(),
                userDTO.getBirthDate(),
                userDTO.getEmail(),
                userDTO.getPhoneNumber()));
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return {@link ResponseEntity} со списком пользователей.
     */
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return {@link ResponseEntity} с пользователем или кодом ошибки, если пользователь не найден.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Возвращает пользователя по его уникальному номеру.
     *
     * @param uniqueNumber Уникальный номер пользователя.
     * @return {@link ResponseEntity} с пользователем или кодом ошибки, если пользователь не найден.
     */
    @GetMapping("/unique/{uniqueNumber}")
    public ResponseEntity<User> getUser(@PathVariable String uniqueNumber) {
        return ResponseEntity.ok(userService.getUserByUniqueNumber(uniqueNumber));
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return {@link ResponseEntity} с сообщением об успешном удалении пользователя.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}