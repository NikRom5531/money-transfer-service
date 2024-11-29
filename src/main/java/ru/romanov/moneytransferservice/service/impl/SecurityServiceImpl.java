package ru.romanov.moneytransferservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.romanov.moneytransferservice.exception.UnauthorizedException;
import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.AuthUser;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.repository.AuthUserRepository;
import ru.romanov.moneytransferservice.repository.UserRepository;
import ru.romanov.moneytransferservice.service.SecurityService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    private final AuthUserRepository authUserRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public String generateToken(AuthUser user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(user.getUser().getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 86_400_000))
                .signWith(key)
                .compact();
    }

    @Override
    public AuthUser getUserByLogin(String login) {
        return authUserRepository.findByUserLogin(login).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        AuthUser authUser = getUserByLogin(username);

        return new org.springframework.security.core.userdetails.User(
                username,
                authUser.getPassword(),
                List.of()
        );
    }

    @Override
    public String extractUserName(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return userDetails.getUsername().equals(claims.getSubject()) && !claims.getExpiration().before(new Date());
    }

    @Override
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailOrPhoneNumber(username, username).orElseThrow(UserNotFoundException::new);
    }
}
