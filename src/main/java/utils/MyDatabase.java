package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String URL = "jdbc:mysql://localhost:3306/choubikloubiki";
    private final String USER = "root";
    private final String PASSWORD = "";
    private static Connection connection;
    private static MyDatabase instance;

    private MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static synchronized MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Always check if the connection is valid before returning it
            if (connection == null || connection.isClosed()) {
                try {
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("Connected to the database.");
                } catch (SQLException e) {
                    System.err.println("Database connection failed: " + e.getMessage());
                    throw new SQLException("Failed to connect to the database.", e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Ensure you declare 'connection' as non-static to avoid unintended sharing across instances


}
