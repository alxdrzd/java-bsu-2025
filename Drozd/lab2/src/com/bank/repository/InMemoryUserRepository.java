package com.bank.repository;

import com.bank.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {
    // Singleton, как и для AccountRepository
    private static InMemoryUserRepository instance;
    private final Map<java.util.UUID, User> storage = new ConcurrentHashMap<>();

    private InMemoryUserRepository() {}

    public static synchronized InMemoryUserRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryUserRepository();
        }
        return instance;
    }

    public void save(User user) {
        storage.put(user.getUUID(), user);
    }

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }
}