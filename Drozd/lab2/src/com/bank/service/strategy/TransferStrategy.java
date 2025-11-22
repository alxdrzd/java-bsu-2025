package com.bank.service.strategy;

import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.model.Account;

import java.util.UUID;

public class TransferStrategy implements TransactionStrategy{
    @Override
    public void execute(Transaction transaction, AccountRepository repository) {
        UUID from = transaction.getAccountOfUser();
        UUID to = transaction.getDestinationAccountId()
                .orElseThrow(() -> new IllegalArgumentException("meow :("));

        if (from.equals(to)) {
            throw new IllegalStateException("the same account!");
        }

        Account fromAcc = repository.findById(from)
                .orElseThrow(() -> new IllegalArgumentException("meow :("));
        Account toAcc = repository.findById(to)
                .orElseThrow(() -> new IllegalArgumentException("meow :("));

        Account lockFirst = fromAcc;
        Account lockSecond = toAcc;

        if (fromAcc.getUUID().compareTo(toAcc.getUUID()) > 0) {
            lockFirst = toAcc;
            lockSecond = fromAcc;
        }

        lockFirst.getLock().lock();
        try {
            lockSecond.getLock().lock();
            try {
                if (fromAcc.getBalance() < transaction.getAmount()) {
                    throw new IllegalArgumentException("Not enough money");
                }

                fromAcc.withdraw(transaction.getAmount());
                toAcc.deposit(transaction.getAmount());

                repository.update(fromAcc);
                repository.update(toAcc);

            } finally {
                lockSecond.getLock().unlock();
            }
        } finally {
            lockFirst.getLock().unlock();
        }
    }
}
