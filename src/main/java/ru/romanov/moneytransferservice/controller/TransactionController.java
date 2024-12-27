package ru.romanov.moneytransferservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.romanov.moneytransferservice.model.response.TransactionResponse;

import java.util.UUID;

/**
 * Интерфейс контроллера для управления транзакциями.
 */
@Tag(name = "Транзакции", description = "Осуществление переводов")
@Validated
public interface TransactionController {

    /**
     * [POST] Выполняет перевод денег между счетами.
     *
     * @param fromAccount Номер счёта, с которого производится перевод.
     * @param toAccount   Номер счёта, на который производится перевод.
     * @param amount      Сумма перевода.
     * @return {@link ResponseEntity} с созданной транзакцией или кодом ошибки.
     */
    @PostMapping("/transfer")
    @Operation(summary = "Перевод денег между счетами", description = "Выполняет перевод указанной суммы между двумя счетами.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод успешно выполнен",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Счёт указанный в запросе не найден",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "409", description = "Счёт отправитель и счёт получатель не должны быть одинаковые",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "503", description = "Сервис недоступен",
                    content = @Content(examples = @ExampleObject()))
    })
    ResponseEntity<TransactionResponse> transferMoney(
            @Valid @RequestParam @NotNull UUID fromAccount,
            @Valid @RequestParam @NotNull UUID toAccount,
            @Valid @RequestParam @DecimalMin(value = "0.01") double amount
    );

    /**
     * [POST] Выполняет зачисление денег на счёт.
     *
     * @param toAccount Номер счёта, на который производится зачисление.
     * @param amount    Сумма зачисления.
     * @return {@link ResponseEntity} с созданной транзакцией.
     */
    @PostMapping("/deposit")
    @Operation(summary = "Зачисление денег на счёт", description = "Выполняет зачисление указанной суммы на счёт.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Зачисление успешно выполнено",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Счёт указанный в запросе не найден",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "503", description = "Сервис недоступен",
                    content = @Content(examples = @ExampleObject()))
    })
    ResponseEntity<TransactionResponse> depositMoney(
            @Valid @RequestParam @NotNull UUID toAccount,
            @Valid @RequestParam @DecimalMin(value = "0.01") double amount
    );

    /**
     * [POST] Выполняет списание денег со счёта.
     *
     * @param fromAccount Номер счёта, с которого производится списание.
     * @param amount      Сумма списания.
     * @return {@link ResponseEntity} с созданной транзакцией.
     */
    @PostMapping("/debit")
    @Operation(summary = "Списание денег со счёта", description = "Выполняет списание указанной суммы со счёта.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Списание успешно выполнено",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Счёт указанный в запросе не найден",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "503", description = "Сервис недоступен",
                    content = @Content(examples = @ExampleObject()))
    })
    ResponseEntity<TransactionResponse> debitMoney(
            @Valid @RequestParam @NotNull UUID fromAccount,
            @Valid @RequestParam @DecimalMin(value = "0.01") double amount
    );
}
