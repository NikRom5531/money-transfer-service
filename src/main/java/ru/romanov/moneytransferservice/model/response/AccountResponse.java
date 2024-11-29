package ru.romanov.moneytransferservice.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "Ответ с информацией о счёте.")
@Builder
@Getter
@Setter
@AllArgsConstructor
public class AccountResponse {

    @NotNull(message = "UID не может быть null")
    private UUID uid;

    @DecimalMin(value = "0.00", message = "Баланс не должен быть меньше 0")
    @NotNull(message = "Баланс не может быть null")
    private double balance;

    @NotBlank(message = "Код валюты не может быть пустым")
    @Size(min = 3, max = 3, message = "Код валюты должен содержать 3 символа")
    private String currency;

    @NotNull(message = "UID владельца не может быть null")
    private UUID ownerUid;
}
