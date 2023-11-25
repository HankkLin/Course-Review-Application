package edu.virginia.sde.reviews;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// UserDatabaseManager.java
public class UserDatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:user.sqlite";

    static {
        createTableIfNotExists();
    }

    private static void createTableIfNotExists() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, password TEXT)";
            String createCourseTableQuery = "CREATE TABLE IF NOT EXISTS courses (subject TEXT, number INTEGER, title TEXT, rating REAL, PRIMARY KEY(subject, number, title))";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createUserTableQuery);
                statement.execute(createCourseTableQuery);
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
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Course> searchCourses(String subject, int number, String title) {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM courses");
            if (!subject.isEmpty()) {
                queryBuilder.append(" WHERE subject = ?");
            }
            if (number != 0) {
                if (!subject.isEmpty()) {
                    queryBuilder.append(" AND");
                } else {
                    queryBuilder.append(" WHERE");
                }
                queryBuilder.append(" number = ?");
            }
            if (!title.isEmpty()) {
                if (!subject.isEmpty() || number != 0) {
                    queryBuilder.append(" AND");
                } else {
                    queryBuilder.append(" WHERE");
                }
                queryBuilder.append(" title LIKE ?");
            }
            //System.out.println(queryBuilder.toString());
            try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
                int parameterIndex = 1;
                if (!subject.isEmpty()) {
                    statement.setString(parameterIndex++, subject);
                }
                if (number != 0) {
                    statement.setInt(parameterIndex++, number);
                }
                if (!title.isEmpty()) {
                    statement.setString(parameterIndex, "%" + title + "%");
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Course course = new Course();
                        course.setSubject(resultSet.getString("subject"));
                        course.setNumber(resultSet.getInt("number"));
                        course.setTitle(resultSet.getString("title"));
                        course.setRating(resultSet.getDouble("rating"));
                        courses.add(course);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }
}