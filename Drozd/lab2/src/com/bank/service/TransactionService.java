package com.bank.service;

import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.service.strategy.*;
import java.util.concurrent.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionService {
    private final AccountRepository repository;
    private final Map<Transaction.TransactionType, TransactionStrategy> strategyMap;
    private final ExecutorService executorService;
    private final List<TransactionListener> listeners = new CopyOnWriteArrayList<>();
    public TransactionService(AccountRepository repository) {
        this.repository = repository; // Теперь мы используем тот репозиторий, который нам дали
        this.executorService = Executors.newFixedThreadPool(4);

        this.strategyMap = new EnumMap<>(Transaction.TransactionType.class);

        // Инициализация стратегий
        strategyMap.put(Transaction.TransactionType.FREEZE, new FreezeStrategy());
        strategyMap.put(Transaction.TransactionType.DEPOSIT, new DepositStrategy());
        strategyMap.put(Transaction.TransactionType.WITHDRAW, new WithdrawalStrategy());
        strategyMap.put(Transaction.TransactionType.TRANSFER, new TransferStrategy());
    }

    public void processTransaction(Transaction transaction) {
        executorService.submit(() -> {
            try {
                TransactionStrategy strategy = strategyMap.get(transaction.getType());
                if (strategy == null) {
                    throw new IllegalStateException("no type :(");
                }

                strategy.execute(transaction, repository);
                notifySuccess(transaction);


            } catch (Exception e) {
                notifyFailure(transaction, e);
            }

        });
    }


    public void addListener(TransactionListener listener) {
        listeners.add(listener);
    }

    private void notifySuccess(Transaction transaction) {
        for (TransactionListener listener : listeners) {
            listener.onTransactionSuccess(transaction);
        }
    }
    private void notifyFailure(Transaction transaction, Exception e) {
        for (TransactionListener listener : listeners) {
            listener.onTransactionFailure(transaction, e);
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
