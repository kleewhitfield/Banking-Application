package bankmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SignUp extends JFrame {
    // Constructor to set up the frame
    SignUp() {
        setTitle("Sign Up - Automated Teller Machine (ATM)");
        setSize(800, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ensure the application exits on close
        setLocation(350, 200);
        setLayout(new BorderLayout());

        // Create the panel for the sign up form
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        // Create GridBagConstraints for layout configuration
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Add registration message
        JLabel registrationLabel = new JLabel("Register User");
        registrationLabel.setFont(new Font("Arial", Font.BOLD, 24));
        registrationLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registrationLabel, gbc);

        // Add name label and text field
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(nameField, gbc);

        // Add address label and text field
        JLabel addressLabel = new JLabel("Address:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(addressLabel, gbc);

        JTextField addressField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(addressField, gbc);

        // Add phone number label and text field
        JLabel phoneLabel = new JLabel("Phone Number:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(phoneField, gbc);

        // Add email label and text field
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(emailField, gbc);

        // Add PIN label and password field
        JLabel pinLabel = new JLabel("PIN:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(pinLabel, gbc);

        JPasswordField pinField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(pinField, gbc);

        // Add sign up button
        JButton signUpButton = new JButton("Sign Up");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(signUpButton, gbc);

        // Add action listener to sign up button to check if fields are not empty and generate card number
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().isEmpty() || addressField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty() || String.valueOf(pinField.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Generate card number
                    String cardNumber = generateCardNumber();
                    // Insert user data into the database
                    DatabaseConnection.insertUser(
                            nameField.getText(),
                            addressField.getText(),
                            phoneField.getText(),
                            emailField.getText(),
                            String.valueOf(pinField.getPassword()),
                            cardNumber
                    );
                    // Show success message
                    JOptionPane.showMessageDialog(null, "Sign Up Successful\nYour Card Number is: " + cardNumber, "Info", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the sign-up frame
                }
            }
        });

        // Add the panel to the frame
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Method to generate a random card number
    private String generateCardNumber() {
        Random rand = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(rand.nextInt(10));
        }
        return cardNumber.toString();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUp());
    }
}
