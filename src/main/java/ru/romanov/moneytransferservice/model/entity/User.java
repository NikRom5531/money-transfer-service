package ru.romanov.moneytransferservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Сущность представляет собой пользователя системы денежных переводов.
 */
@Getter
@Setter
@Entity(name = "users")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String uniqueNumber;
    private String lastName;
    private String firstName;
    private String patronymicName;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
}
