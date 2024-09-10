package ru.romanov.moneytransferservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.romanov.moneytransferservice.model.dto.UserDto;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.service.ValidationService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationServiceImplTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationServiceImpl();
    }

    @Test
    void testIsUserValidated_ValidUser() {
        User user = new User();
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPatronymicName("Middle");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("+71234567890");

        assertTrue(validationService.isUserValidated(user));
    }

    @Test
    void testIsUserValidated_InvalidUser() {
        User user = new User();
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPatronymicName("Middle");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setEmail("invalid-email");
        user.setPhoneNumber("invalid-phone");

        assertFalse(validationService.isUserValidated(user));
    }

    @Test
    void testIsUserDtoValidated_ValidUserDto() {
        UserDto userDto = new UserDto();
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setPatronymicName("Middle");
        userDto.setBirthDate(LocalDate.parse("2000-01-01"));
        userDto.setEmail("john.doe@example.com");
        userDto.setPhoneNumber("+71234567890");

        assertTrue(validationService.isUserDtoValidated(userDto));
    }

    @Test
    void testIsUserDtoValidated_InvalidUserDto() {
        UserDto userDto = new UserDto();
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setPatronymicName("Middle");
        userDto.setBirthDate(LocalDate.now());
        userDto.setEmail("invalid-email");
        userDto.setPhoneNumber("invalid-phone");

        assertFalse(validationService.isUserDtoValidated(userDto));
    }

    @Test
    void testIsDateValidated_ValidDate() {
        assertTrue(validationService.isDateValidated("01/01/2000"));
        assertTrue(validationService.isDateValidated("01-01-2000"));
        assertTrue(validationService.isDateValidated("01.01.2000"));
        assertTrue(validationService.isDateValidated("2000/01/01"));
        assertTrue(validationService.isDateValidated("2000-01-01"));
        assertTrue(validationService.isDateValidated("2000.01.01"));
    }

    @Test
    void testIsDateValidated_InvalidDate() {
        assertFalse(validationService.isDateValidated("01-01/2000"));           // Не соответствует формату
        assertFalse(validationService.isDateValidated("2000\\01\\01"));         // Не соответствует формату
        assertFalse(validationService.isDateValidated("31-02-2024"));           // Неверный день
        assertFalse(validationService.isDateValidated("2024-13-01"));           // Неверный месяц
        assertFalse(validationService.isDateValidated("2024-01-01T12:00:00"));  // Время включено
        assertFalse(validationService.isDateValidated("2024-01-01 12:00"));     // Неверный формат с временем
        assertFalse(validationService.isDateValidated("invalid-date"));         // Некорректный формат
    }

    @Test
    void testIsEmailValidated_ValidEmail() {
        assertTrue(validationService.isEmailValidated("john.doe@example.com"));
    }

    @Test
    void testIsEmailValidated_InvalidEmail() {
        assertFalse(validationService.isEmailValidated("invalid-email"));
    }

    @Test
    void testIsPhoneNumberValidated_ValidPhoneNumber() {
        assertTrue(validationService.isPhoneNumberValidated("+71234567890"));
        assertTrue(validationService.isPhoneNumberValidated("81234567890"));
    }

    @Test
    void testIsPhoneNumberValidated_InvalidPhoneNumber() {
        assertFalse(validationService.isPhoneNumberValidated("1234567890"));
        assertFalse(validationService.isPhoneNumberValidated("invalid-phone"));
    }

    @Test
    void testIsStringValidated_ValidString() {
        assertTrue(validationService.isStringValidated("John Doe"));
        assertTrue(validationService.isStringValidated("Иван Иванович"));
    }

    @Test
    void testIsStringValidated_InvalidString() {
        assertFalse(validationService.isStringValidated("John123"));
        assertFalse(validationService.isStringValidated("!@#$%^&*()"));
    }
}
