package com.bank.ui;

import com.bank.model.User;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.service.TransactionService;
import com.bank.service.TransactionFactory;
import com.bank.service.TransactionListener;
import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;
import com.bank.repository.InMemoryUserRepository;

import javax.swing.*;
import java.awt.*;
// ...

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.UUID;

public class BankUI extends JFrame implements TransactionListener {

    private final TransactionService transactionService;
    private final AccountRepository accountRepository;
    private final InMemoryUserRepository userRepository;

    private JTextArea logArea;
    private JTextArea userListArea;

    private JTextField amountField;
    private JTextField userUuidField;
    private JTextField accountUuidField;
    private JTextField targetAccountUuidField;

    private JTextField nicknameField;

    public BankUI() {
        this.accountRepository = InMemoryAccountRepository.getInstance();
        this.userRepository = InMemoryUserRepository.getInstance(); // Инициализируем юзеров
        this.transactionService = new TransactionService(this.accountRepository);

        setTitle("Bank System Lab UI");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Вкладки
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Users & Registration", createUserPanel());
        tabbedPane.add("Transactions", createOperationsPanel());

        add(tabbedPane, BorderLayout.CENTER);
        logArea = new JTextArea(8, 20);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Async Transaction Log"));
        add(logScroll, BorderLayout.SOUTH);
    }

    private JPanel createUserPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Nickname:"));
        nicknameField = new JTextField(15);
        inputPanel.add(nicknameField);

        JButton createBtn = new JButton("Create User & Account");
        createBtn.setBackground(new Color(144, 238, 144));
        inputPanel.add(createBtn);

        userListArea = new JTextArea();
        userListArea.setEditable(false);
        userListArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        userListArea.setText("--- REGISTERED USERS WILL APPEAR HERE ---\n");
        JScrollPane scrollPane = new JScrollPane(userListArea);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        createBtn.addActionListener(e -> {
            String nick = nicknameField.getText();
            if (nick == null || nick.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a nickname!");
                return;
            }

            User newUser = new User(nick);
            userRepository.save(newUser);

            Account newAccount = new Account(newUser.getUUID());
            accountRepository.save(newAccount);

            appendUserToDisplay(newUser, newAccount);

            nicknameField.setText("");
            log("System: Created user " + nick + " and account " + newAccount.getUUID());
        });

        return mainPanel;
    }

    private void appendUserToDisplay(User u, Account a) {
        String info = String.format(
                "Nick: %-10s | UserUUID: %-36s | AccountUUID: %-36s\n",
                u.getNickname(), u.getUUID(), a.getUUID()
        );
        userListArea.append(info);
        userListArea.setCaretPosition(userListArea.getDocument().getLength());
    }

    private JPanel createOperationsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("User UUID:"));
        userUuidField = new JTextField();
        panel.add(userUuidField);

        panel.add(new JLabel("Account UUID (Source):"));
        accountUuidField = new JTextField();
        panel.add(accountUuidField);

        panel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        panel.add(amountField);

        panel.add(new JLabel("Target Account UUID (Transfer only):"));
        targetAccountUuidField = new JTextField();
        panel.add(targetAccountUuidField);

        JButton btnDeposit = new JButton("DEPOSIT (+)");
        JButton btnWithdraw = new JButton("WITHDRAW (-)");
        JButton btnTransfer = new JButton("TRANSFER (->)");
        JButton btnFreeze = new JButton("FREEZE (*)");

        btnDeposit.addActionListener(e -> sendTransaction(Transaction.TransactionType.DEPOSIT));
        btnWithdraw.addActionListener(e -> sendTransaction(Transaction.TransactionType.WITHDRAW));
        btnFreeze.addActionListener(e -> sendTransaction(Transaction.TransactionType.FREEZE));
        btnTransfer.addActionListener(e -> sendTransaction(Transaction.TransactionType.TRANSFER));

        panel.add(btnDeposit);
        panel.add(btnWithdraw);
        panel.add(btnTransfer);
        panel.add(btnFreeze);

        return panel;
    }

    private void sendTransaction(Transaction.TransactionType type) {
        try {
            if (userUuidField.getText().isEmpty() || accountUuidField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill User UUID and Account UUID");
                return;
            }

            UUID userUUID = UUID.fromString(userUuidField.getText().trim());
            UUID accUUID = UUID.fromString(accountUuidField.getText().trim());
            double amount = 0;

            if (type != Transaction.TransactionType.FREEZE) {
                String amountText = amountField.getText();
                if (amountText.isEmpty()) amountText = "0";
                amount = Double.parseDouble(amountText);
            }

            Transaction tx;
            if (type == Transaction.TransactionType.TRANSFER) {
                String targetTxt = targetAccountUuidField.getText().trim();
                if (targetTxt.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Target Account UUID is required for transfer");
                    return;
                }
                UUID targetUUID = UUID.fromString(targetTxt);
                tx = TransactionFactory.createTransfer(amount, userUUID, accUUID, targetUUID);
            } else if (type == Transaction.TransactionType.DEPOSIT) {
                tx = TransactionFactory.createDeposit(amount, userUUID, accUUID);
            } else if (type == Transaction.TransactionType.WITHDRAW) {
                tx = TransactionFactory.createWithdrawal(amount, userUUID, accUUID);
            } else {
                tx = TransactionFactory.createFreeze(0, userUUID, accUUID);
            }

            log("Sending transaction: " + type + "...");
            transactionService.processTransaction(tx);

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid number format (UUID or Amount)");
        } catch (Exception ex) {
            log("Error creating request: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    @Override
    public void onTransactionSuccess(Transaction transaction) {
        log("✅ SUCCESS: " + transaction.getType() + " | ID: " + transaction.getUuid());
        accountRepository.findById(transaction.getAccountOfUser())
                .ifPresent(acc -> log("   New Balance (Source): " + acc.getBalance()));
    }

    @Override
    public void onTransactionFailure(Transaction transaction, Exception e) {
        log("❌ FAILED: " + transaction.getType() + " | Reason: " + e.getMessage());
    }

    public TransactionService getService() {
        return transactionService;
    }
}