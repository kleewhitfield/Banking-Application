package bankmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private JTextField cardField;
    private JPasswordField pinField;

    // Constructor to set up the frame
    Login() {
        setTitle("Automated Teller Machine (ATM)");
        setSize(800, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application exits on close
        setLocation(350, 200);
        setLayout(new BorderLayout());

        // Create the panel for the login form
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        // Create GridBagConstraints for layout configuration
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Add welcome message
        JLabel welcomeLabel = new JLabel("Welcome to ATM");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(welcomeLabel, gbc);

        // Add card number label and text field
        JLabel cardLabel = new JLabel("Card Number:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(cardLabel, gbc);

        cardField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(cardField, gbc);

        // Add PIN label and password field
        JLabel pinLabel = new JLabel("PIN:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(pinLabel, gbc);

        pinField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(pinField, gbc);

        // Add sign in button
        JButton signInButton = new JButton("Sign In");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(signInButton, gbc);

        // Add action listener to sign in button to check if fields are not empty
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cardField.getText().isEmpty() || String.valueOf(pinField.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Card Number and PIN cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Proceed with login logic
                    authenticateUser();
                }
            }
        });

        // Add sign up button
        JButton signUpButton = new JButton("Sign Up");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(signUpButton, gbc);

        // Add action listener to sign up button to open the sign up frame
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUp();
            }
        });

        // Add the panel to the frame
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Method to authenticate user from the database
    private void authenticateUser() {
        String cardNumber = cardField.getText();
        String pin = String.valueOf(pinField.getPassword());

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT name, balance FROM users WHERE card_number = ? AND pin = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pin);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String userName = rs.getString("name");
                double balance = rs.getDouble("balance");
                new MainMenu(userName, cardNumber, balance);
                dispose(); // Close the login frame
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Card Number or PIN", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
