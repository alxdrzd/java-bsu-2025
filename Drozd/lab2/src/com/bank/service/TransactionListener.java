package com.bank.service;

import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.service.strategy.*; // Импортируем все стратегии
import java.util.concurrent.*;

public interface TransactionListener {
    void onTransactionSuccess(Transaction transaction);
    void onTransactionFailure(Transaction transaction, Exception e);
}
