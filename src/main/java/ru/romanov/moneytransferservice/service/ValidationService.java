package ru.romanov.moneytransferservice.service;

import ru.romanov.moneytransferservice.model.dto.UserDto;
import ru.romanov.moneytransferservice.model.entity.User;

/**
 * Интерфейс сервиса валидации данных пользователей.
 */
public interface ValidationService {
    /**
     * Проверяет валидность пользователя.
     *
     * @param user Пользователь для проверки.
     * @return {@code true}, если пользователь валиден, иначе {@code false}.
     */
    boolean isUserValidated(User user);

    /**
     * Проверяет валидность DTO пользователя.
     *
     * @param userDto Пользователь DTO для проверки.
     * @return {@code true}, если пользователь валиден, иначе {@code false}.
     */
    boolean isUserDtoValidated(UserDto userDto);

    /**
     * Проверяет валидность даты по заданным шаблонам.
     *
     * @param date Строка с датой для проверки.
     * @return {@code true}, если дата валидна, иначе {@code false}.
     */
    boolean isDateValidated(String date);

    /**
     * Проверяет валидность email.
     *
     * @param email Email для проверки.
     * @return {@code true}, если email валиден, иначе {@code false}.
     */
    boolean isEmailValidated(String email);

    /**
     * Проверяет валидность номера телефона.
     *
     * @param phoneNumber Номер телефона для проверки.
     * @return {@code true}, если номер телефона валиден, иначе {@code false}.
     */
    boolean isPhoneNumberValidated(String phoneNumber);

    /**
     * Проверяет валидность строки по общему регулярному выражению.
     *
     * @param string Строка для проверки.
     * @return {@code true}, если строка валидна, иначе {@code false}.
     */
    boolean isStringValidated(String string);
}
