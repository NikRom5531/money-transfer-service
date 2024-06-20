package ru.romanov.moneytransferservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import ru.romanov.moneytransferservice.model.dto.UserDto;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.service.ValidationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Реализация сервиса валидации данных пользователей.
 */
@Slf4j
@Service
public class ValidationServiceImpl implements ValidationService {
    private static final String REGEX_FOR_STRING_VALIDATION = "[\\Dа-яА-Яa-zA-Z -]+";
    private static final String REGEX_FOR_PHONE = "(\\+7|8)\\d{10}";
    private static final String[] LOCAL_DATE_INPUT_PATTERNS = {"dd.MM.yyyy", "dd-MM-yyyy", "dd/MM/yyyy", "yyyy.MM.dd", "yyyy-MM-dd", "yyyy/MM/dd"};

    /**
     * Проверяет валидность пользователя.
     *
     * @param user Пользователь для проверки
     * @return true, если пользователь валиден, иначе false
     */
    @Override
    public boolean isUserValidated(User user) {
        return isUserValidated(user.getLastName(), user.getFirstName(), user.getPatronymicName(), user.getBirthDate().toString(), user.getEmail(), user.getPhoneNumber());
    }

    /**
     * Проверяет валидность DTO пользователя.
     *
     * @param userDto Пользователь DTO для проверки
     * @return true, если пользователь валиден, иначе false
     */
    @Override
    public boolean isUserDtoValidated(UserDto userDto) {
        return isUserValidated(userDto.getLastName(), userDto.getFirstName(), userDto.getPatronymicName(), userDto.getBirthDate().toString(), userDto.getEmail(), userDto.getPhoneNumber());
    }

    /**
     * Проверяет валидность DTO пользователя.
     *
     * @param lastName       Фамилия пользователя
     * @param firstName      Имя пользователя
     * @param patronymicName Отчество пользователя (может быть пустым)
     * @param birthDate      Дата рождения пользователя в виде строки (должна быть валидной датой)
     * @param email          Email пользователя
     * @param phoneNumber    Номер телефона пользователя
     * @return true, если DTO пользователя валидно, иначе false
     */
    private boolean isUserValidated(String lastName, String firstName, String patronymicName, String birthDate, String email, String phoneNumber) {
        boolean isValid = true;
        String errorMessage = "";
        if (!isStringValidated(lastName)) {
            errorMessage += "last name is invalid";
            isValid = false;
        }
        if (!isStringValidated(firstName)) {
            errorMessage += (errorMessage.isEmpty() ? "" : ", ") + "first name is invalid";
            isValid = false;
        }
        if (!isStringValidated(patronymicName) && !patronymicName.isEmpty()) {
            errorMessage += (errorMessage.isEmpty() ? "" : ", ") + "patronymic name is invalid";
            isValid = false;
        }
        if (!isDateValidated(birthDate)) {
            errorMessage += (errorMessage.isEmpty() ? "" : ", ") + "birth date is invalid";
            isValid = false;
        }
        if (!isEmailValidated(email)) {
            errorMessage += (errorMessage.isEmpty() ? "" : ", ") + "email is invalid";
            isValid = false;
        }
        if (!isPhoneNumberValidated(phoneNumber)) {
            errorMessage += (errorMessage.isEmpty() ? "" : ", ") + "phone number is invalid";
            isValid = false;
        }
        if (!isValid) log.error("User is not valid: \n{}.", errorMessage);
        return isValid;
    }

    /**
     * Проверяет валидность даты по заданным шаблонам.
     *
     * @param date Строка с датой для проверки
     * @return true, если дата валидна, иначе false
     */
    @Override
    public boolean isDateValidated(String date) {
        return Arrays.stream(LOCAL_DATE_INPUT_PATTERNS).map(DateTimeFormatter::ofPattern).anyMatch(formatter -> {
            try {
                LocalDate.parse(date, formatter);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    /**
     * Проверяет валидность email.
     *
     * @param email Email для проверки
     * @return true, если email валиден, иначе false
     */
    @Override
    public boolean isEmailValidated(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    /**
     * Проверяет валидность номера телефона.
     *
     * @param phoneNumber Номер телефона для проверки
     * @return true, если номер телефона валиден, иначе false
     */
    @Override
    public boolean isPhoneNumberValidated(String phoneNumber) {
        return phoneNumber.matches(REGEX_FOR_PHONE);
    }

    /**
     * Проверяет валидность строки по общему регулярному выражению.
     *
     * @param string Строка для проверки
     * @return true, если строка валидна, иначе false
     */
    @Override
    public boolean isStringValidated(String string) {
        return string.matches(REGEX_FOR_STRING_VALIDATION);
    }
}
