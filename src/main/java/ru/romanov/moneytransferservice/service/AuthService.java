package ru.romanov.moneytransferservice.service;

import ru.romanov.moneytransferservice.model.entity.AuthUser;
import ru.romanov.moneytransferservice.model.request.LoginRequest;
import ru.romanov.moneytransferservice.model.request.RegisterRequest;
import ru.romanov.moneytransferservice.model.response.TokenResponse;

public interface AuthService {

    AuthUser registerUser(RegisterRequest request);

    TokenResponse loginUser(LoginRequest request);

    void logout();
}
