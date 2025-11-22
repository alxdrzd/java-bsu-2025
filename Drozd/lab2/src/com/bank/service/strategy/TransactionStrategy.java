package com.bank.service.strategy;

import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;

public interface TransactionStrategy {
    void execute(Transaction transaction, AccountRepository repository);
}
