package ru.romanov.moneytransferservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.moneytransferservice.model.dto.TransferRequest;
import ru.romanov.moneytransferservice.service.MoneyTransferService;

@RestController
@RequestMapping("/api/transfer")
public class MoneyTransferController {

    @Autowired
    private MoneyTransferService moneyTransferService;

    @PostMapping
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        moneyTransferService.transfer(request);
        return ResponseEntity.ok("Transfer successful");
    }
}