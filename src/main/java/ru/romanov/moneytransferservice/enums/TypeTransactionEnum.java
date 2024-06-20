package ru.romanov.moneytransferservice.enums;

/**
 * Перечисление для типов транзакций.
 * <ul>
 *     <li>TRANSFER - перевод средств между счетами</li>
 *     <li>DEPOSIT - зачисление средств на счет</li>
 *     <li>DEBIT - списание средств со счета</li>
 * </ul>
 */
public enum TypeTransactionEnum {
    TRANSFER,
    DEPOSIT,
    DEBIT
}
