package ru.romanov.moneytransferservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) для пользователя.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String lastName;
    private String firstName;
    private String patronymicName;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
}