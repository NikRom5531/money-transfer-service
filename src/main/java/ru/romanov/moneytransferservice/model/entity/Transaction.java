package ru.romanov.moneytransferservice.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.romanov.moneytransferservice.model.enums.TypeTransactionEnum;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность представляет собой финансовую транзакцию между банковскими счетами.
 */
@Getter
@Setter
@Entity
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность представляет собой финансовую транзакцию между банковскими счетами")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Уникальный идентификатор транзакции", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uid;

    @NotNull(message = "Дата транзакции не может быть null")
    @PastOrPresent(message = "Дата транзакции не может быть в будущем")
    @Schema(description = "Дата и время транзакции", example = "2024-11-16T12:00:00", format = "date-time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime transactionDate;

    @NotNull(message = "Тип транзакции не может быть null")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Тип транзакции", example = "TRANSFER", allowableValues = "TRANSFER, DEPOSIT, DEBIT", requiredMode = Schema.RequiredMode.REQUIRED)
    private TypeTransactionEnum type;

    @ManyToOne
    @JoinColumn(name = "from_account_uid")
    @Schema(description = "Счет отправителя", requiredMode = Schema.RequiredMode.REQUIRED)
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_uid")
    @Schema(description = "Счет получателя", requiredMode = Schema.RequiredMode.REQUIRED)
    private Account toAccount;

    @NotNull(message = "Сумма не может быть null")
    @DecimalMin(value = "0.01", message = "Сумма транзакции должна быть больше 0")
    @Schema(description = "Сумма транзакции", example = "100.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private double amount;

    @NotBlank(message = "Код валюты не может быть пустым")
    @Size(min = 3, max = 3, message = "Код валюты должен содержать 3 символа")
    @Schema(description = "Код валюты (например, USD, EUR)", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 3, minLength = 3)
    private String currencyCode;
}