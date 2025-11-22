package com.bank.service.strategy;

import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.model.Account;

public class DepositStrategy implements TransactionStrategy {
    @Override
    public void execute(Transaction transaction, AccountRepository repository) {
        Account account = repository.findById(transaction.getAccountOfUser())
                .orElseThrow(() -> new IllegalArgumentException("meow :("));
        account.deposit(transaction.getAmount());
        repository.update(account);
    }
}
