package ru.romanov.moneytransferservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.moneytransferservice.client.CurrencyConverterClient;
import ru.romanov.moneytransferservice.exception.ForbiddenException;
import ru.romanov.moneytransferservice.exception.TransferYourselfException;
import ru.romanov.moneytransferservice.model.entity.Account;
import ru.romanov.moneytransferservice.model.entity.Transaction;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.model.enums.TypeTransactionEnum;
import ru.romanov.moneytransferservice.repository.TransactionRepository;
import ru.romanov.moneytransferservice.service.AccountService;
import ru.romanov.moneytransferservice.service.AuthService;
import ru.romanov.moneytransferservice.service.SecurityService;
import ru.romanov.moneytransferservice.service.TransactionService;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Реализация сервиса для выполнения транзакций между счетами.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final SecurityService securityService;
    private final CurrencyConverterClient currencyConverterClient;

    @Override
    public Transaction createTransaction(
            UUID fromAccountNumber,
            UUID toAccountNumber,
            TypeTransactionEnum type,
            double amount,
            String currencyCode
    ) {
        return transactionRepository.save(
                Transaction.builder()
                        .transactionDate(LocalDateTime.now())
                        .fromAccount(accountService.getAccountByAccountNumber(fromAccountNumber))
                        .toAccount(accountService.getAccountByAccountNumber(toAccountNumber))
                        .type(type)
                        .amount(amount)
                        .currencyCode(currencyCode)
                        .build());
    }

    @Override
    @Transactional
    public Transaction transferMoney(
            UUID fromAccountNumber,
            UUID toAccountNumber,
            double amount
    ) {
        Account fromAccount = accountService.getAccountByAccountNumber(fromAccountNumber);
        checkAccess(fromAccount);

        if (fromAccountNumber.equals(toAccountNumber)) throw new TransferYourselfException();
        Account toAccount = accountService.getAccountByAccountNumber(toAccountNumber);

        double convertedAmount;

        if (!fromAccount.getCurrency().equals(toAccount.getCurrency()))
            convertedAmount = currencyConverterClient.convert(fromAccount.getCurrency(), toAccount.getCurrency(), amount);
        else convertedAmount = amount;

        accountService.updateAccountBalance(fromAccountNumber, TypeTransactionEnum.DEBIT, amount);
        accountService.updateAccountBalance(toAccountNumber, TypeTransactionEnum.DEPOSIT, convertedAmount);

        return createTransaction(fromAccountNumber, toAccountNumber, TypeTransactionEnum.TRANSFER, amount, fromAccount.getCurrency());
    }

    @Override
    @Transactional
    public Transaction depositMoney(
            UUID toAccountNumber,
            double amount
    ) {
        Account toAccount = accountService.getAccountByAccountNumber(toAccountNumber);
        accountService.updateAccountBalance(toAccountNumber, TypeTransactionEnum.DEPOSIT, amount);

        return createTransaction(null, toAccountNumber, TypeTransactionEnum.DEPOSIT, amount, toAccount.getCurrency());
    }

    @Override
    @Transactional
    public Transaction debitMoney(
            UUID fromAccountNumber,
            double amount
    ) {
        Account fromAccount = accountService.getAccountByAccountNumber(fromAccountNumber);
        checkAccess(fromAccount);

        accountService.updateAccountBalance(fromAccountNumber, TypeTransactionEnum.DEBIT, amount);

        return createTransaction(fromAccountNumber, null, TypeTransactionEnum.DEBIT, amount, fromAccount.getCurrency());
    }

    private void checkAccess(Account account) {
        User user = securityService.getCurrentUser();

        if (!account.getOwner().getUid().equals(user.getUid())) {
            throw new ForbiddenException("You are not owner of this account");
        }
    }
}
