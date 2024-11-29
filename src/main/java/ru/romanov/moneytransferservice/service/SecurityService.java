package ru.romanov.moneytransferservice.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.romanov.moneytransferservice.model.entity.AuthUser;
import ru.romanov.moneytransferservice.model.entity.User;

public interface SecurityService {

    String generateToken(AuthUser user);

    AuthUser getUserByLogin(String login);

    UserDetails loadUserByUsername(String username);

    String extractUserName(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    User getCurrentUser();
}
