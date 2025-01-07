package com.xero.SwiftPay.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/Galgotias";
    private static final String USER = "root";
    private static final String PASSWORD = "654975";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    // Method to get a connection to the database
    public static Connection getConnection() throws SQLException {
        try {
            // Load the database driver
            Class.forName(DRIVER_CLASS);

            // Establish and return a connection to the database
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found", e);
        } catch (SQLException e) {
            throw new SQLException("Error connecting to the database", e);
        }
    }
}
