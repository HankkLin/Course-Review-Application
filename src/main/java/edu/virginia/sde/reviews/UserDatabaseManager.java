package edu.virginia.sde.reviews;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// UserDatabaseManager.java
public class UserDatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:user.sqlite";

    static {
        createTableIfNotExists();
    }

    private static void createTableIfNotExists() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, password TEXT)";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidUser(String userName, String password) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String query = "SELECT * FROM users WHERE userName = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // User is valid if there's a match
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean userExist(String userName) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String query = "SELECT * FROM users WHERE userName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addUser(String userName, String password) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String query = "INSERT INTO users (userName, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, password);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0; // User added successfully if rows affected > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}