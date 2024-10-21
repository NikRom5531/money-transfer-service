package ru.romanov.moneytransferservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Сущность представляет собой пользователя системы денежных переводов.
 */
@Getter
@Setter
@Entity(name = "users")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String lastName;

    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;

    @Size(max = 50, message = "Отчество не может содержать более 50 символов")
    private String patronymicName;

    @NotNull(message = "Дата рождения не может быть null")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @NotNull(message = "Email не может быть null")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "^\\+?[0-9\\-\\s]{7,15}$", message = "Некорректный формат номера телефона")
    private String phoneNumber;
}
