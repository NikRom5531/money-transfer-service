package ru.romanov.moneytransferservice.enums;

/**
 * Перечисление для типов транзакций:
 * <ul>
 *     <li>TRANSFER - Перевод средств между счетами.</li>
 *     <li>DEPOSIT - Зачисление средств на счет.</li>
 *     <li>DEBIT - Списание средств со счета.</li>
 * </ul>
 */
public enum TypeTransactionEnum {
    TRANSFER,
    DEPOSIT,
    DEBIT
}
