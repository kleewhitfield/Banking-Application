package bankmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionHistory extends JFrame {
    public TransactionHistory(String cardNumber) {
        setTitle("Transaction History");
        setSize(800, 480);
        setLocation(350, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM transactions WHERE user_id = (SELECT id FROM users WHERE card_number = ?) ORDER BY timestamp DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String transactionType = rs.getString("transaction_type");
                double amount = rs.getDouble("amount");
                String timestamp = rs.getString("timestamp");
                textArea.append(transactionType + ": $" + amount + " on " + timestamp + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(panel);

        setVisible(true);
    }
}
