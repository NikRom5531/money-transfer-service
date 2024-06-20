package ru.romanov.moneytransferservice.service;

import ru.romanov.moneytransferservice.model.dto.UserDto;
import ru.romanov.moneytransferservice.model.entity.User;

/**
 * Интерфейс сервиса валидации данных пользователей.
 */
public interface ValidationService {
    boolean isUserValidated(User user);

    boolean isUserDtoValidated(UserDto userDto);

    boolean isDateValidated(String date);

    boolean isEmailValidated(String email);

    boolean isPhoneNumberValidated(String phoneNumber);

    boolean isStringValidated(String string);
}
