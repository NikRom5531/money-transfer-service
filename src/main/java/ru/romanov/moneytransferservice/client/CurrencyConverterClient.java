package ru.romanov.moneytransferservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "currency-converter-client", url = "${currency.converter.service.url}")
public interface CurrencyConverterClient {

    @GetMapping("/api/currency/convert")
    BigDecimal convert(@RequestParam("amount") BigDecimal amount, @RequestParam("from") String fromCurrency, @RequestParam("to") String toCurrency);
}