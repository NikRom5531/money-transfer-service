package ru.romanov.moneytransferservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.romanov.moneytransferservice.enums.TypeTransactionEnum;

import java.time.LocalDateTime;

/**
 * Сущность представляет собой финансовую транзакцию между банковскими счетами.
 */
@Getter
@Setter
@Entity
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime transactionDate;
    @Enumerated(EnumType.STRING)
    private TypeTransactionEnum type;
    private String fromAccountNumber;
    private String toAccountNumber;
    private double amount;
    private String currencyCode;
}