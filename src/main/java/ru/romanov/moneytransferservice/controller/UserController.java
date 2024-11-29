package ru.romanov.moneytransferservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.model.request.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс контроллера для управления пользователями.
 */
@Tag(name = "Пользователи", description = "Управления пользователями")
@Validated
public interface UserController {

    /*
    /**
     * [POST] Создает нового пользователя.
     *
     * @param request Данные нового пользователя.
     * @return {@link ResponseEntity} с созданным пользователем или кодом ошибки.
     */
    /*
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание нового пользователя",
            description = "Создает нового пользователя на основе переданных данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 400,
                                "error": "Bad Request",
                                "path": "/api/users"
                            }"""))),
            @ApiResponse(responseCode = "409", description = "Указанный email или номер телефона уже зарегистрированы",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 409,
                                "error": "Conflict",
                                "path": "/api/users"
                            }"""))),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/users"
                            }""")))
    })
    ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request);// */

    /**
     * [GET] Возвращает список всех пользователей.
     *
     * @return {@link ResponseEntity} со списком пользователей.
     */
    @GetMapping
    @Operation(summary = "Получение всех пользователей",
            description = "Возвращает список всех зарегистрированных пользователей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "204", description = "Список пользователей пуст",
                    content = @Content(examples = @ExampleObject(value = "[]"))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/users"
                            }""")))
    })
    ResponseEntity<List<User>> getUsers();

    /**
     * [PATCH] Обновляет пользователя по указанному UID в {@link UpdateUserRequest}.
     *
     * @param request Данные для обновления пользователя.
     * @return {@link ResponseEntity} с созданным пользователем или кодом ошибки.
     */
    @PatchMapping
    @Operation(summary = "Обновление пользователя по UID указанному в теле запроса",
            description = "Обновляет информацию о пользователе по его уникальному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлён",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 400,
                                "error": "Bad Request",
                                "path": "/api/users"
                            }"""))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 404,
                                "error": "Not Found",
                                "path": "/api/users"
                            }"""))),
            @ApiResponse(responseCode = "409", description = "Указанный email или номер телефона уже зарегистрированы",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 409,
                                "error": "Conflict",
                                "path": "/api/users"
                            }"""))),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/users"
                            }""")))
    })
    ResponseEntity<User> updateUser(@Valid @RequestBody UpdateUserRequest request);

    /**
     * [GET] Возвращает пользователя по его идентификатору.
     *
     * @param uid Идентификатор пользователя.
     * @return {@link ResponseEntity} с пользователем или кодом ошибки, если пользователь не найден.
     */
    @GetMapping("/{uid}")
    @Operation(summary = "Получение пользователя по UID",
            description = "Возвращает информацию о пользователе по его уникальному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно найден",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 404,
                                "error": "Not Found",
                                "path": "/api/users/123e4567-e89b-12d3-a456-426614174000"
                            }"""))),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/users/123e4567-e89b-12d3-a456-426614174000"
                            }""")))
    })
    ResponseEntity<User> getUser(@Valid @PathVariable @NotNull UUID uid);

    /**
     * [DELETE] Удаляет пользователя по его идентификатору.
     *
     * @param uid Идентификатор пользователя.
     * @return {@link ResponseEntity} с сообщением об успешном удалении пользователя.
     */
    @DeleteMapping("/{uid}")
    @Operation(summary = "Удаление пользователя", description = "Удаляет пользователя на основе его уникального идентификатора.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён",
                    content = @Content(examples = @ExampleObject(value = "[]"))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 404,
                                "error": "Not Found",
                                "path": "/api/users/123e4567-e89b-12d3-a456-426614174000"
                            }"""))),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/users/123e4567-e89b-12d3-a456-426614174000"
                            }""")))
    })
    ResponseEntity<String> deleteUser(@Valid @PathVariable @NotNull UUID uid);

    /**
     * [DELETE] Удаляет текущего пользователя.
     *
     * @return {@link ResponseEntity} с сообщением об успешном удалении пользователя.
     */
    @DeleteMapping("/current")
    @Operation(summary = "Удаление текущего пользователя", description = "Удаляет текущего пользователя авторизованного в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён",
                    content = @Content(examples = @ExampleObject(value = "[]"))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 404,
                                "error": "Not Found",
                                "path": "/api/users/123e4567-e89b-12d3-a456-426614174000"
                            }"""))),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/users/123e4567-e89b-12d3-a456-426614174000"
                            }""")))
    })
    ResponseEntity<String> deleteCurrentUser();
}