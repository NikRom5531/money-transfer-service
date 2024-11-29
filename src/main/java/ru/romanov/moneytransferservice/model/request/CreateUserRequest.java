package ru.romanov.moneytransferservice.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Запрос для создания сущности пользователя")
public class CreateUserRequest {

    @NotBlank(message = "Фамилия не может быть пустой")
    @Schema(description = "Фамилия пользователя",
            example = "Иванов",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotBlank(message = "Имя не может быть пустым")
    @Schema(description = "Имя пользователя",
            example = "Иван",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @Size(max = 50, message = "Отчество не может содержать более 50 символов")
    @Schema(description = "Отчество пользователя",
            example = "Сергеевич",
            maxLength = 50)
    private String patronymicName;

    @NotNull(message = "Дата рождения не может быть null")
    @Past(message = "Дата рождения должна быть в прошлом")
    @Schema(description = "Дата рождения пользователя",
            example = "1990-05-15",
            format = "date",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate birthDate;

    @NotNull(message = "Email не может быть null")
    @Email(message = "Некорректный формат email")
    @Schema(description = "Электронная почта пользователя",
            example = "example@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "((\\+7|8)\\d{10})", message = "Некорректный формат номера телефона")
    @Schema(description = "Номер телефона пользователя",
            example = "+79161234567",
            requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "(\\+7|8)\\d{10}")
    private String phoneNumber;
}
