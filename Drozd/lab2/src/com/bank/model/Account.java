package com.bank.model;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    final private UUID uuid;
    private final UUID userOwnerId;
    private double balance;
    private boolean frozen;

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", uuid=" + uuid +
                ", frozen=" + frozen +
                '}';
    }

    public Account(UUID userOwnerId) {
        this(UUID.randomUUID(), userOwnerId, 0.0, false);
    }

    public Account(UUID uuid, UUID userOwnerId, double balance, boolean isFrozen) {
        this.userOwnerId = userOwnerId;
        this.uuid = uuid;
        this.balance = balance;
        this.frozen = isFrozen;
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public boolean isFrozen() {
        lock.lock();
        try {
            return frozen;
        } finally {
            lock.unlock();
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public void deposit(double money) {
        lock.lock();
        try {
            if (this.isFrozen()) {
                throw new IllegalStateException("The account is frozen");
            }
            this.balance += money;
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double money) {
        lock.lock();
        try {
            if (this.isFrozen()) {
                throw new IllegalStateException("The account is frozen");
            }
            if (money > this.balance) {
                throw new IllegalArgumentException("Not enough money :( ");
            }
            this.balance -= money;
        } finally {
            lock.unlock();
        }
    }

    public void freeze() {
        lock.lock();
        try {
            this.frozen = true;
        } finally {
            lock.unlock();
        }
    }

    public void unfreeze() {
        lock.lock();
        try {
            this.frozen = false;
        } finally {
            lock.unlock();
        }
    }


    public ReentrantLock getLock() {
        return lock;
    }

    public UUID getUserOwnerId() {
        return userOwnerId;
    }
}
