package ru.romanov.moneytransferservice.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.romanov.moneytransferservice.model.enums.TypeTransactionEnum;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Ответ с информацией о транзакции.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Ответ с информацией о транзакции.")
public class TransactionResponse {

    @Schema(description = "Дата и время выполнения транзакции.", example = "2023-11-08T14:30:00")
    @NotNull(message = "Дата транзакции не может быть null")
    @PastOrPresent(message = "Дата транзакции не может быть в будущем")
    private LocalDateTime transactionDate;

    @NotNull(message = "Тип транзакции не может быть null")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Тип транзакции", example = "TRANSFER", allowableValues = "TRANSFER, DEPOSIT, DEBIT")
    private TypeTransactionEnum type;

    @Schema(description = "Идентификатор счёта, с которого производится транзакция.", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID fromAccountUid;

    @Schema(description = "Идентификатор счёта, на который производится транзакция.", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID toAccountUid;

    @Schema(description = "Сумма транзакции.", example = "150.75")
    @NotNull(message = "Сумма не может быть null")
    @DecimalMin(value = "0.01", message = "Сумма транзакции должна быть больше 0")
    private double amount;

    @Schema(description = "Код валюты транзакции в формате ISO 4217.", example = "USD")
    @NotBlank(message = "Код валюты не может быть пустым")
    @Size(min = 3, max = 3, message = "Код валюты должен содержать 3 символа")
    private String currencyCode;
}
