package ru.romanov.moneytransferservice.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для обновления сущности пользователя")
public class UpdateUserRequest {

    @Schema(description = "Уникальный идентификатор пользователя",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uid;

    @Schema(description = "Фамилия пользователя",
            example = "Иванов")
    private String lastName;

    @Schema(description = "Имя пользователя",
            example = "Иван")
    private String firstName;

    @Size(max = 50, message = "Отчество не может содержать более 50 символов")
    @Schema(description = "Отчество пользователя",
            example = "Сергеевич",
            maxLength = 50)
    private String patronymicName;

    @Past(message = "Дата рождения должна быть в прошлом")
    @Schema(description = "Дата рождения пользователя",
            example = "1990-05-15",
            format = "date")
    private LocalDate birthDate;

    @Email(message = "Некорректный формат email")
    @Schema(description = "Электронная почта пользователя",
            example = "example@example.com")
    private String email;

    @Pattern(regexp = "((\\+7|8)\\d{10})", message = "Некорректный формат номера телефона")
    @Schema(description = "Номер телефона пользователя",
            example = "+79161234567",
            pattern = "(\\+7|8)\\d{10}")
    private String phoneNumber;
}
