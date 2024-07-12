package bankmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Deposit extends JFrame {
    private String cardNumber;
    private MainMenu mainMenu;

    public Deposit(MainMenu mainMenu, String cardNumber) {
        this.mainMenu = mainMenu;
        this.cardNumber = cardNumber;

        setTitle("Deposit");
        setSize(400, 300);
        setLocation(350, 200);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel amountLabel = new JLabel("Amount:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(amountLabel, gbc);

        JTextField amountField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(amountField, gbc);

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(null, "Amount must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        depositAmount(amount);
                        JOptionPane.showMessageDialog(null, "Deposit Successful", "Info", JOptionPane.INFORMATION_MESSAGE);
                        mainMenu.updateBalance(getUpdatedBalance());
                        mainMenu.updateChart();
                        dispose();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(depositButton, gbc);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void depositAmount(double amount) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Update the user's balance
            String updateBalanceSql = "UPDATE users SET balance = balance + ? WHERE card_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateBalanceSql)) {
                pstmt.setDouble(1, amount);
                pstmt.setString(2, cardNumber);
                pstmt.executeUpdate();
            }

            // Insert a transaction record
            String insertTransactionSql = "INSERT INTO transactions (user_id, transaction_type, amount) " +
                                          "VALUES ((SELECT id FROM users WHERE card_number = ?), 'Deposit', ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertTransactionSql)) {
                pstmt.setString(1, cardNumber);
                pstmt.setDouble(2, amount);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double getUpdatedBalance() {
        double newBalance = 0.00;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String getBalanceSql = "SELECT balance FROM users WHERE card_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(getBalanceSql)) {
                pstmt.setString(1, cardNumber);
                var rs = pstmt.executeQuery();
                if (rs.next()) {
                    newBalance = rs.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newBalance;
    }
}
