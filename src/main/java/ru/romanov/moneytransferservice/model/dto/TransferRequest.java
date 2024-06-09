package ru.romanov.moneytransferservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    private BigDecimal amount;
    private String fromCurrency;
    private String toCurrency;
    private String fromAccount;
    private String toAccount;
}
