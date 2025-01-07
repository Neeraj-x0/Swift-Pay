package com.xero.SwiftPay.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xero.SwiftPay.model.User;

public class UserDao {

    private Connection connection;

    @SuppressWarnings("CallToPrintStackTrace")
    public UserDao() {
        // Get DB connection from DataSource or DriverManager
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Galgotias", "root", "654975");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Register user
    public void registerUser(User user) {
        try {
            String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Authenticate user
    public boolean authenticate(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get user by username
    public User getUser(String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("email"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
