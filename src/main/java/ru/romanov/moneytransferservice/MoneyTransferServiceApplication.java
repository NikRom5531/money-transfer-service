package ru.romanov.moneytransferservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MoneyTransferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferServiceApplication.class, args);
    }

}
