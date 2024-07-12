package bankmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class MainMenu extends JFrame {
    private String userName;
    private String cardNumber;
    private double balance;
    private JLabel balanceLabel;
    private JPanel chartPanelContainer; // Panel to hold the chart panel

    public MainMenu(String userName, String cardNumber, double balance) {
        this.userName = userName;
        this.cardNumber = cardNumber;
        this.balance = balance;

        setTitle("ATM Main Menu");
        setSize(1200, 480); // Increase width to accommodate chart
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(350, 200);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome, " + userName);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(welcomeLabel, gbc);

        JLabel cardLabel = new JLabel("Card Number: " + cardNumber);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(cardLabel, gbc);

        balanceLabel = new JLabel("Balance: $" + balance);
        gbc.gridy = 2;
        panel.add(balanceLabel, gbc);

        JButton transactionHistoryButton = new JButton("Transaction History");
        transactionHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TransactionHistory(cardNumber);
            }
        });
        gbc.gridy = 3;
        panel.add(transactionHistoryButton, gbc);

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Deposit(MainMenu.this, cardNumber);
            }
        });
        gbc.gridy = 4;
        panel.add(depositButton, gbc);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Withdraw(MainMenu.this, cardNumber);
            }
        });
        gbc.gridy = 5;
        panel.add(withdrawButton, gbc);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the main menu frame
                new Login(); // Reopen the login frame
            }
        });
        gbc.gridy = 6;
        panel.add(logoutButton, gbc);

        add(panel, BorderLayout.WEST); // Add panel to the left side

        // Create a panel to hold the chart and add it to the right side
        chartPanelContainer = new JPanel();
        chartPanelContainer.setLayout(new BorderLayout());
        chartPanelContainer.setPreferredSize(new Dimension(600, 480));
        add(chartPanelContainer, BorderLayout.EAST);

        // Initial update of the chart
        updateChart();

        setVisible(true);
    }

    public void updateBalance(double newBalance) {
        this.balance = newBalance;
        balanceLabel.setText("Balance: $" + newBalance);
    }

    public void updateChart() {
        chartPanelContainer.removeAll(); // Remove previous chart
        ChartPanel chartPanel = TransactionChart.createChartPanel(cardNumber);
        chartPanel.setPreferredSize(new Dimension(600, 480)); // Set preferred size for the chart
        chartPanelContainer.add(chartPanel, BorderLayout.CENTER);
        chartPanelContainer.revalidate(); // Refresh the panel
        chartPanelContainer.repaint(); // Repaint the panel
    }


    public static void main(String[] args) {
    }
}
