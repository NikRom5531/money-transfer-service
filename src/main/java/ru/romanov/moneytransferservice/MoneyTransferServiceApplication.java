package ru.romanov.moneytransferservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Главный класс приложения для перевода денежных средств.
 * <p>
 * Этот класс инициализирует приложение Spring Boot для работы с сервисом перевода денег.
 * Он использует аннотации {@code @SpringBootApplication} для автоматической настройки Spring Boot и {@code @EnableFeignClients}
 * для включения поддержки Feign клиентов, что позволяет использовать HTTP-клиенты для взаимодействия с другими микросервисами.
 * <p>
 * В методе {@code main} создается экземпляр {@code SpringApplication}, который запускает приложение Spring Boot.
 */
@SpringBootApplication
@EnableFeignClients
public class MoneyTransferServiceApplication {
    /**
     * Точка входа в приложение.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferServiceApplication.class, args);
    }
}
