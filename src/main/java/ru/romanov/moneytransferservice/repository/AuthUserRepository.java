package ru.romanov.moneytransferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.romanov.moneytransferservice.model.entity.AuthUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthUserRepository  extends JpaRepository<AuthUser, UUID> {

    @Query(value = """
            SELECT au.* FROM auth_users au
            JOIN users u ON au.user_uid = u.uid
            WHERE u.email = :login OR u.phone_number = :login""", nativeQuery = true)
    Optional<AuthUser> findByUserLogin(String login);
}
