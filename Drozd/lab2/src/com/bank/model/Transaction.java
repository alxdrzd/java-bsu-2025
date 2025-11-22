package com.bank.model;

import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

public class Transaction {
    final private UUID uuid;
    final private LocalDateTime timestamp;
    final private TransactionType type;
    final private double amount;
    final private UUID UserUUID;
    final private UUID AccountOfUser;
    final private UUID AccountOfDestination;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        FREEZE,
        TRANSFER
    }

    public Transaction(TransactionType type, double amount, UUID userUUID, UUID accountOfUser, UUID AccountOfDestination) {
        this.uuid = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.UserUUID = userUUID;
        this.AccountOfUser = accountOfUser;
        this.AccountOfDestination = AccountOfDestination;
    }

    public Transaction(TransactionType type, double amount, UUID userUUID, UUID accountOfUser) {
        this(type, amount, userUUID, accountOfUser, null);
    }

    @Override
    public String toString() {

        if (this.AccountOfDestination == null) {
            return "Transaction{" +
                    "uuid=" + uuid +
                    ", timestamp=" + timestamp +
                    ", type=" + type +
                    ", amount=" + amount +
                    ", UserUUID=" + UserUUID +
                    ", AccountOfUser=" + AccountOfUser +
                    '}';
        }

        return "Transaction{" +
                "uuid=" + uuid +
                ", timestamp=" + timestamp +
                ", type=" + type +
                ", amount=" + amount +
                ", UserUUID=" + UserUUID +
                ", AccountOfUser=" + AccountOfUser +
                ", AccountOfDestination" + AccountOfDestination +
                '}';

    }

    public UUID getAccountOfUser() {
        return AccountOfUser;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public UUID getUserUUID() {
        return UserUUID;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Optional<UUID> getDestinationAccountId() {
        return Optional.ofNullable(AccountOfDestination);
    }
}
