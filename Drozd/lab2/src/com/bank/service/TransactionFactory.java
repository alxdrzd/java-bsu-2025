package com.bank.service;

import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.service.strategy.*;
import java.util.concurrent.*;

import java.util.UUID;

public class TransactionFactory {
    public static Transaction createDeposit(double amount, UUID userUUUID, UUID accountUUID) {
        return new Transaction(Transaction.TransactionType.DEPOSIT, amount, userUUUID, accountUUID);
    }
    public static Transaction createWithdrawal(double amount, UUID userUUUID, UUID accountUUID) {
        return new Transaction(Transaction.TransactionType.WITHDRAW, amount, userUUUID, accountUUID);
    }
    public static Transaction createFreeze(double amount, UUID userUUUID, UUID accountUUID) {
        return new Transaction(Transaction.TransactionType.FREEZE, 0, userUUUID, accountUUID);
    }
    public static Transaction createTransfer(double amount, UUID userUUUID, UUID accountUUID,  UUID toAccount) {
        return new Transaction(Transaction.TransactionType.TRANSFER, amount, userUUUID, accountUUID, toAccount);
    }
}
