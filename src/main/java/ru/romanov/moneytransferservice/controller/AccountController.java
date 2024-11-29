package ru.romanov.moneytransferservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.romanov.moneytransferservice.model.response.AccountResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Интерфейс контроллера для управления счетами.
 */
@Tag(name = "Счета", description = "Управления счетами пользователей")
@Validated
public interface AccountController {

    /**
     * [POST] Создает новый счёт.
     *
     * @param currency Код валюты.
     * @return {@link ResponseEntity} с созданным счётом или кодом ошибки.
     */
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Operation(summary = "Создать новый счёт", description = "Создаёт новый счёт с указанной валютой и владельцем.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Счёт успешно создан",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 400,
                                "error": "Bad Request",
                                "path": "/api/account"
                            }"""))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Пользователь по указанному UID не найден",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 404,
                                "error": "Not Found",
                                "path": "/api/account"
                            }"""))),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/account"
                            }"""))),
            @ApiResponse(responseCode = "503", description = "Сервис недоступен",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 503,
                                "error": "Service Unavailable",
                                "path": "/api/account"
                            }""")))
    })
    ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestParam @NotBlank String currency
    );

    /**
     * [GET] Возвращает список всех счетов.
     *
     * @return {@link ResponseEntity} со списком счетов или кодом ошибки.
     */
    @GetMapping("/list")
    @Operation(summary = "Получить список всех счетов", description = "Возвращает список всех счетов в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список счетов получен",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class)))),
            @ApiResponse(responseCode = "204", description = "Список счетов пуст",
                    content = @Content(examples = @ExampleObject(value = "[]"))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/account/list"
                            }""")))
    })
    ResponseEntity<List<AccountResponse>> getAccounts();

    /**
     * [GET] Возвращает информацию о счёте по его номеру.
     *
     * @param uid Номер счёта.
     * @return {@link ResponseEntity} с информацией о счёте или кодом ошибки.
     */
    @GetMapping
    @Operation(summary = "Получить информацию о счёте", description = "Возвращает информацию о счёте по его номеру.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о счёте получена",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 404,
                                "error": "Not Found",
                                "path": "/api/account"
                            }"""))),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/account"
                            }""")))
    })
    ResponseEntity<AccountResponse> getAccount(@Valid @RequestParam @NotNull UUID uid);

    /**
     * [GET] Возвращает список всех счетов пользователя.
     *
     * @return {@link ResponseEntity} со списком счетов или кодом ошибки.
     */
    @GetMapping("/list-by-user")
    @Operation(summary = "Получить список всех счетов пользователя",
            description = "Возвращает список всех счетов пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список счетов получен",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class)))),
            @ApiResponse(responseCode = "204", description = "Список счетов пуст",
                    content = @Content(examples = @ExampleObject(value = "[]"))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/account/list-by-user"
                            }""")))
    })
    ResponseEntity<List<AccountResponse>> getAccountsByUserUid();

    /**
     * [GET] Возвращает карту поддерживаемых валют.
     *
     * @return {@link ResponseEntity} с картой поддерживаемых валют.
     */
    @GetMapping("/supported-currency-map")
    @Operation(summary = "Получить карту поддерживаемых валют",
            description = "Возвращает список поддерживаемых валют.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта поддерживаемых валют получена",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "AED": "Дирхам ОАЭ",
                              "AMD": "Армянский драм",
                              ...
                              "ZAR": "Южноафриканский рэнд"
                            }"""))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/account/supported-currency-map"
                            }"""))),
            @ApiResponse(responseCode = "503", description = "Сервис недоступен",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 503,
                                "error": "Service Unavailable",
                                "path": "/api/account/supported-currency-map"
                            }""")))
    })
    ResponseEntity<Map<String, String>> getSupportedCurrencyMap();

    /**
     * [DELETE] Удаляет счёт по его номеру.
     *
     * @param uid Номер счёта.
     * @return {@link ResponseEntity} с сообщением об успешном удалении или кодом ошибки.
     */
    @DeleteMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Operation(summary = "Удалить счёт", description = "Удаляет счёт по указанному номеру.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Счёт успешно удалён",
                    content = @Content(examples = @ExampleObject(value = "[]"))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 404,
                                "error": "Not Found",
                                "path": "/api/account"
                            }"""))),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-11-15T18:51:12.001+00:00",
                                "status": 500,
                                "error": "Internal Server Error",
                                "path": "/api/account"
                            }""")))
    })
    ResponseEntity<String> deleteAccount(@Valid @RequestParam @NotNull UUID uid);
}
