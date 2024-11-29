package ru.romanov.moneytransferservice.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Сущность представляет собой банковский счет пользователя.
 */
@Getter
@Setter
@Entity
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность представляет банковский счет пользователя.")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Уникальный идентификатор счета", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uid;

    @DecimalMin(value = "0.00", message = "Баланс не должен быть меньше 0")
    @NotNull(message = "Баланс не может быть null")
    @Schema(description = "Баланс счета", example = "1000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private double balance;

    @NotBlank(message = "Код валюты не может быть пустым")
    @Size(min = 3, max = 3, message = "Код валюты должен содержать 3 символа")
    @Schema(description = "Код валюты (например, USD, EUR)", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 3, minLength = 3)
    private String currency;

    @NotNull(message = "Владелец не может быть null")
    @ManyToOne
    @JoinColumn(name = "owner_uid", nullable = false)
    @Schema(description = "Владелец счета", requiredMode = Schema.RequiredMode.REQUIRED)
    private User owner;
}
