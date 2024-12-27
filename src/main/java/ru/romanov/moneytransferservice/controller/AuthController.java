package ru.romanov.moneytransferservice.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.romanov.moneytransferservice.model.request.LoginRequest;
import ru.romanov.moneytransferservice.model.request.RegisterRequest;
import ru.romanov.moneytransferservice.model.response.TokenResponse;
import ru.romanov.moneytransferservice.model.response.UserResponse;

@Tag(name = "Авторизация", description = "Регистрация и вход в систему пользователей")
public interface AuthController {

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя", description = "Создаёт пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Регистрация успешно пройдена",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject()))
    })
    ResponseEntity<?> register(@RequestBody RegisterRequest request);

    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя", description = "Генерирует токен доступа для пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Авторизация успешно пройдена",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject()))
    })
    ResponseEntity<?> login(@RequestBody LoginRequest request);

    @Hidden
    @PostMapping("/logout")
    @Operation(summary = "Выход из системы", description = "Пользователь.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Регистрация успешно пройдена",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject()))
    })
    ResponseEntity<?> logout();
}
