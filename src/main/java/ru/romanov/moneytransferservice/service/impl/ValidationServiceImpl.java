package ru.romanov.moneytransferservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import ru.romanov.moneytransferservice.model.dto.UserDto;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.service.ValidationService;

import java.util.regex.Pattern;

/**
 * Реализация сервиса валидации данных пользователей.
 */
@Slf4j
@Service
public class ValidationServiceImpl implements ValidationService {
    private static final String REGEX_FOR_STRING_VALIDATION = "^[а-яА-Яa-zA-Z ]+$";
    private static final String REGEX_FOR_PHONE = "(\\+7|8)\\d{10}";
    private static final String[] DATE_PATTERNS = {
            "^(\\d{2})\\.(\\d{2})\\.(\\d{4})$", // dd.MM.yyyy
            "^(\\d{2})-(\\d{2})-(\\d{4})$",     // dd-MM-yyyy
            "^(\\d{2})/(\\d{2})/(\\d{4})$",     // dd/MM/yyyy
            "^(\\d{4})\\.(\\d{2})\\.(\\d{2})$", // yyyy.MM.dd
            "^(\\d{4})-(\\d{2})-(\\d{2})$",     // yyyy-MM-dd
            "^(\\d{4})/(\\d{2})/(\\d{2})$"      // yyyy/MM/dd
    };

    @Override
    public boolean isUserValidated(User user) {
        return isUserValidated(user.getLastName(), user.getFirstName(), user.getPatronymicName(), user.getBirthDate().toString(), user.getEmail(), user.getPhoneNumber());
    }

    @Override
    public boolean isUserDtoValidated(UserDto userDto) {
        return isUserValidated(userDto.getLastName(), userDto.getFirstName(), userDto.getPatronymicName(), userDto.getBirthDate().toString(), userDto.getEmail(), userDto.getPhoneNumber());
    }

    @Override
    public boolean isDateValidated(String date) {
        for (String pattern : DATE_PATTERNS) {
            Pattern regex = Pattern.compile(pattern);
            var matcher = regex.matcher(date);
            if (matcher.matches()) {
                int year = Integer.parseInt(matcher.group(matcher.group(1).length() == 4 ? 1 : 3));
                int month = Integer.parseInt(matcher.group(2));
                int day = Integer.parseInt(matcher.group(matcher.group(1).length() == 4 ? 3 : 1));
                if (isValidDayMonthYear(day, month, year)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmailValidated(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    @Override
    public boolean isPhoneNumberValidated(String phoneNumber) {
        return phoneNumber.matches(REGEX_FOR_PHONE);
    }

    @Override
    public boolean isStringValidated(String string) {
        return string.matches(REGEX_FOR_STRING_VALIDATION);
    }

    /**
     * Проверяет валидность DTO пользователя.
     *
     * @param lastName       Фамилия пользователя.
     * @param firstName      Имя пользователя.
     * @param patronymicName Отчество пользователя (может быть пустым).
     * @param birthDate      Дата рождения пользователя в виде строки (должна быть валидной датой).
     * @param email          Email пользователя.
     * @param phoneNumber    Номер телефона пользователя.
     * @return {@code true}, если DTO пользователя валидно, иначе {@code false}.
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
        if (!patronymicName.isEmpty() && !isStringValidated(patronymicName)) {
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
     * Проверяет, является ли переданная комбинация дня, месяца и года допустимой.
     *
     * @param day   День, который нужно проверить (от 1 до 31 в зависимости от месяца).
     * @param month Месяц, который нужно проверить (от 1 до 12).
     * @param year  Год, который нужно проверить.
     * @return {@code true}, если комбинация день-месяц-год является допустимой, иначе {@code false}.
     */
    private boolean isValidDayMonthYear(int day, int month, int year) {
        if (month < 1 || month > 12) return false;
        int[] daysInMonth = {31, isLeapYear(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return 1 <= day && day <= daysInMonth[month - 1];
    }

    /**
     * Проверяет, является ли указанный год високосным.
     * <p>Правило високосного года: год является високосным, если он кратен 4,
     * но не кратен 100, за исключением тех случаев, когда он кратен 400.</p>
     *
     * @param year Год, который нужно проверить.
     * @return {@code true}, если год високосный, иначе {@code false}.
     */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}