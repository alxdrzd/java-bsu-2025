package com.bank.repository;

import com.bank.model.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepository{
    private static InMemoryAccountRepository instance;

    @Override
    public void save(Account account) {
        storage.put(account.getUUID(), account);
    }

    @Override
    public void update(Account account) {
        if (storage.containsKey(account.getUUID())) {
            storage.put(account.getUUID(), account);
        }
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    private final Map<UUID, Account> storage = new ConcurrentHashMap<>();

    private InMemoryAccountRepository() {}

    public static synchronized InMemoryAccountRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryAccountRepository();
        }
        return instance;
    }



}
