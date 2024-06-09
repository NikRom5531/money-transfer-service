package ru.romanov.moneytransferservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.romanov.moneytransferservice.client.CurrencyConverterClient;
import ru.romanov.moneytransferservice.model.dto.TransferRequest;

import java.math.BigDecimal;

@Service
public class MoneyTransferService {

    @Autowired
    private CurrencyConverterClient currencyConverterClient;

    public void transfer(TransferRequest request) {
        BigDecimal convertedAmount = currencyConverterClient.convert(request.getAmount(), request.getFromCurrency(), request.getToCurrency());
        // Логика перевода средств между счетами
    }
}