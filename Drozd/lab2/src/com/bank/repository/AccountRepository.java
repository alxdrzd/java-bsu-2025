package com.bank.repository;

import com.bank.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    void save(Account account);
    Optional<Account> findById(UUID id);
    void update(Account account);
}