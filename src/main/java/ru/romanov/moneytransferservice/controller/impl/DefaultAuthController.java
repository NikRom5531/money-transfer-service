package ru.romanov.moneytransferservice.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.moneytransferservice.controller.AuthController;
import ru.romanov.moneytransferservice.model.request.LoginRequest;
import ru.romanov.moneytransferservice.model.request.RegisterRequest;
import ru.romanov.moneytransferservice.service.AuthService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class DefaultAuthController implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.loginUser(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> logout() {
        try {
            authService.logout();
            return ResponseEntity.ok("Logout");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logout failed: " + e.getMessage());
        }
    }
}
