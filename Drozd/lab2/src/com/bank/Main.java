package com.bank;

import com.bank.ui.BankUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                BankUI ui = new BankUI();

                ui.getService().addListener(ui);

                ui.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}