package ru.romanov.moneytransferservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.romanov.moneytransferservice.config.CurrencyConverterConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Клиент Feign для взаимодействия с сервисом конвертации валют.
 * Аннотация {@code @FeignClient} определяет имя клиента и URL сервиса,
 * а также конфигурацию для этого клиента.
 */
@FeignClient(name = "currency-converter-client", url = "${currency.converter.service.url}", configuration = CurrencyConverterConfiguration.class)
public interface CurrencyConverterClient {

    /**
     * Метод для конвертации валюты.
     *
     * @param fromCurrency из какой валюты
     * @param toCurrency в какую валюту
     * @param amount сумма для конвертации
     * @return конвертированная сумма
     */
    @GetMapping("/api/currency/convert")
    Double convert(@RequestParam("from") String fromCurrency, @RequestParam("to") String toCurrency, @RequestParam("amount") Double amount);

    /**
     * Метод для получения списка поддерживаемых валютных кодов.
     *
     * @return список поддерживаемых валютных кодов
     */
    @GetMapping("/api/currency/supported-codes")
    List<String> supportedCodes();

    /**
     * Метод для получения карты поддерживаемых валют.
     *
     * @return карта, где ключом является валютный код, а значением - название валюты
     */
    @GetMapping("/supported-currency-map")
    Map<String, String> supportedCurrencyMap();
}