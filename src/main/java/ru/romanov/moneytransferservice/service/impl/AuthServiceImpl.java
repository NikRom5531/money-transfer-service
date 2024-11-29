package ru.romanov.moneytransferservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.romanov.moneytransferservice.exception.UnauthorizedException;
import ru.romanov.moneytransferservice.model.entity.AuthUser;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.model.request.LoginRequest;
import ru.romanov.moneytransferservice.model.request.RegisterRequest;
import ru.romanov.moneytransferservice.model.response.TokenResponse;
import ru.romanov.moneytransferservice.repository.AuthUserRepository;
import ru.romanov.moneytransferservice.service.AuthService;
import ru.romanov.moneytransferservice.service.SecurityService;
import ru.romanov.moneytransferservice.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final UserService userService;
    private final SecurityService securityService;

    @Override
    public AuthUser registerUser(RegisterRequest request) {
        if (!request.getPassword().equals(request.getPasswordConfirm())) return null;

        User user = userService.createUser(request);

        return authUserRepository.save(
                AuthUser.builder()
                        .user(user)
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build());
    }

    @Override
    public TokenResponse loginUser(LoginRequest request) {
        var user = securityService.getUserByLogin(request.getLogin());

        if (!new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        } else {
            var jwt = securityService.generateToken(user);

            return new TokenResponse(jwt);
        }
    }

    @Override
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
