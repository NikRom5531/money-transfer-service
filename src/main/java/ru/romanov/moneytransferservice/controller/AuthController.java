package ru.romanov.moneytransferservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.romanov.moneytransferservice.model.request.LoginRequest;
import ru.romanov.moneytransferservice.model.request.RegisterRequest;

@Tag(name = "Авторизация", description = "Регистрация и вход в систему пользователей")
public interface AuthController {

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody RegisterRequest request);

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest request);

    @PostMapping("/logout")
    ResponseEntity<?> logout();
}
