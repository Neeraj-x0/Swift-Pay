package com.xero.SwiftPay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@SpringBootApplication
public class MyApplication {

    @Autowired
    private DataSource dataSource;

    // Root route: Return a JSP view for home page
    @RequestMapping("/")
    public String home() {
        return "home"; // Resolves to /WEB-INF/jsp/home.jsp
    }

    // /user route: Fetch student details from the database
    // Payment initiation route: Display the payment form
    @RequestMapping("/payment/{userId}")
    public String paymentPage(@PathVariable String userId, Model model) {
        model.addAttribute("userId", userId);
        return "enterAmount"; // Resolves to /WEB-INF/jsp/enterAmount.jsp
    }

    // Payment processing route: Handle the form submission
    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("amount") String amount,
            @RequestParam("userId") String userId,
            Model model) {
        // Check if amount is valid
        if (amount == null || amount.trim().isEmpty() || !isNumeric(amount)) {
            model.addAttribute("amountAlert", "Please enter a valid amount.");
            model.addAttribute("userId", userId);
            return "enterAmount"; // Return back to the payment page with an error
        }

        // Process the payment (You can integrate actual payment gateway logic here)
        boolean paymentSuccess = processPaymentTransaction(userId, Double.parseDouble(amount));

        if (paymentSuccess) {
            model.addAttribute("paymentSuccess", true);
        } else {
            model.addAttribute("paymentSuccess", false);
        }

        return "paymentResult"; // Resolves to /WEB-INF/jsp/paymentResult.jsp
    }

    private boolean processPaymentTransaction(String userId, double amount) {
        try (Connection connection = dataSource.getConnection()) {
            // Check if the user has enough balance
            String balanceCheckSql = "SELECT balance FROM bank_account WHERE user_id = ?";
            PreparedStatement balanceCheckStmt = connection.prepareStatement(balanceCheckSql);
            balanceCheckStmt.setString(1, userId);
            ResultSet balanceResult = balanceCheckStmt.executeQuery();

            // If user doesn't exist or insufficient balance
            if (!balanceResult.next() || balanceResult.getDouble("balance") < amount) {
                return false; // Insufficient balance or user not found
            }

            // Deduct amount from user's account
            String updateBalanceSql = "UPDATE bank_account SET balance = balance - ? WHERE user_id = ?";
            PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceSql);
            updateBalanceStmt.setDouble(1, amount);
            updateBalanceStmt.setString(2, userId);
            int rowsUpdated = updateBalanceStmt.executeUpdate();

            if (rowsUpdated > 0) {
                // Record the transaction in the transactions table
                String insertTransactionSql = "INSERT INTO transactions (user_id, amount, transaction_type) VALUES (?, ?, ?)";
                PreparedStatement insertTransactionStmt = connection.prepareStatement(insertTransactionSql);
                insertTransactionStmt.setString(1, userId);
                insertTransactionStmt.setDouble(2, amount);
                insertTransactionStmt.setString(3, "debit"); // 'debit' for payment
                insertTransactionStmt.executeUpdate();

                return true; // Transaction successful
            }

            return false; // Update failed

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Database error
        }
    }

    // Debit transaction route
    @PostMapping("/debit")
    public String debitTransaction(@RequestParam("userId") String userId,
            @RequestParam("amount") String amount,
            Model model) {
        if (amount == null || amount.trim().isEmpty() || !isNumeric(amount)) {
            model.addAttribute("error", "Please enter a valid amount.");
            return "transactionResult"; // Resolves to /WEB-INF/jsp/transactionResult.jsp
        }

        boolean debitSuccess = processDebitTransaction(userId, Double.parseDouble(amount));

        model.addAttribute("debitSuccess", debitSuccess);
        return "transactionResult";
    }

    // Credit transaction route
    @PostMapping("/credit")
    public String creditTransaction(@RequestParam("userId") String userId,
            @RequestParam("amount") String amount,
            Model model) {
        if (amount == null || amount.trim().isEmpty() || !isNumeric(amount)) {
            model.addAttribute("error", "Please enter a valid amount.");
            return "transactionResult"; // Resolves to /WEB-INF/jsp/transactionResult.jsp
        }

        boolean creditSuccess = processCreditTransaction(userId, Double.parseDouble(amount));

        model.addAttribute("creditSuccess", creditSuccess);
        return "transactionResult";
    }

    // View balance route
    @RequestMapping("/balance/{userId}")
    public String viewBalance(@PathVariable String userId, Model model) {
        double balance = getBankBalance(userId);
        model.addAttribute("balance", balance);
        return "balanceView"; // Resolves to /WEB-INF/jsp/balanceView.jsp
    }

    // Transaction history route
    @RequestMapping("/transactions/{userId}")
    public String viewTransactions(@PathVariable String userId, Model model) {
        List<String> transactions = getTransactionHistory(userId);
        model.addAttribute("transactions", transactions);
        return "transactionHistory"; // Resolves to /WEB-INF/jsp/transactionHistory.jsp
    }

    // Helper method to validate if the amount is numeric
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Simulate debit transaction
    private boolean processDebitTransaction(String userId, double amount) {
        try (Connection connection = dataSource.getConnection()) {
            // Deduct amount from user's balance
            String sql = "UPDATE bank_account SET balance = balance - ? WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, amount);
            statement.setString(2, userId);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Simulate credit transaction
    private boolean processCreditTransaction(String userId, double amount) {
        try (Connection connection = dataSource.getConnection()) {
            // Add amount to user's balance
            String sql = "UPDATE bank_account SET balance = balance + ? WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, amount);
            statement.setString(2, userId);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get bank balance for the user
    private double getBankBalance(String userId) {
        double balance = 0;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT balance FROM bank_account WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                balance = resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    // Get transaction history for the user
    private List<String> getTransactionHistory(String userId) {
        List<String> transactions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM transactions WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String transaction = "ID: " + resultSet.getInt("id") +
                        ", Amount: " + resultSet.getDouble("amount") +
                        ", Date: " + resultSet.getTimestamp("timestamp");
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
