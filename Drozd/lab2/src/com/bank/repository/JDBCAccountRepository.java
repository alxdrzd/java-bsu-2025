package com.bank.repository;

import com.bank.model.*;

import java.sql.*;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JDBCAccountRepository implements AccountRepository{
    private static JDBCAccountRepository instance;

    private final Map<UUID, Account> identityMap = new ConcurrentHashMap<>();

    private static final String URL = "jdbc:mysql://localhost:3306/bank_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private JDBCAccountRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found");
        }
    }

    public static synchronized JDBCAccountRepository getInstance() {
        if (instance == null) {
            instance = new JDBCAccountRepository();
        }
        return instance;
    }


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public void save(Account account) {
        String sql = "INSERT INTO accounts (id, user_owner_id, balance, frozen) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.getUUID().toString());
            statement.setString(2, account.getUserOwnerId().toString());
            statement.setString(3, String.valueOf(account.getBalance()));
            statement.setString(4, String.valueOf(account.isFrozen()));

            statement.executeUpdate();

            identityMap.put(account.getUUID(), account);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Account> findById(UUID id) {
        if (identityMap.containsKey(id)) {
            return Optional.of(identityMap.get(id));
        }

        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Account acc = new Account(
                            UUID.fromString(rs.getString("id")),
                            UUID.fromString(rs.getString("user_owner_id")),
                            rs.getDouble("balance"),
                            rs.getBoolean("frozen")
                    );

                    identityMap.putIfAbsent(acc.getUUID(), acc);

                    return Optional.of(identityMap.get(acc.getUUID()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(Account account) {
        String sql = "UPDATE accounts SET balance = ?, frozen = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, account.getBalance());
            stmt.setBoolean(2, account.isFrozen());
            stmt.setString(3, account.getUUID().toString());

            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Account not found in DB to update: " + account.getUUID());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update account", e);
        }
    }

}
