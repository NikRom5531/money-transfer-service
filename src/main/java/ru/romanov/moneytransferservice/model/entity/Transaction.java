package ru.romanov.moneytransferservice.model.entity;

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
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    @NotNull(message = "Дата транзакции не может быть null")
    @PastOrPresent(message = "Дата транзакции не может быть в будущем")
    private LocalDateTime transactionDate;

    @NotNull(message = "Тип транзакции не может быть null")
    @Enumerated(EnumType.STRING)
    private TypeTransactionEnum type;

    @ManyToOne
    @JoinColumn(name = "from_account_uid")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_uid")
    private Account toAccount;

    @NotNull(message = "Сумма не может быть null")
    @DecimalMin(value = "0.01", message = "Сумма транзакции должна быть больше 0")
    private double amount;

    @NotBlank(message = "Код валюты не может быть пустым")
    @Size(min = 3, max = 3, message = "Код валюты должен содержать 3 символа")
    private String currencyCode;
}