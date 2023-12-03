package edu.virginia.sde.reviews;
import java.sql.*;
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
            String createReviewTableQuery = "CREATE TABLE IF NOT EXISTS reviews (rating INTEGER, comment TEXT, user TEXT, time TEXT, subject TEXT, number INTEGER, title TEXT, " +
                    "FOREIGN KEY(subject, number, title) REFERENCES courses(subject, number, title), FOREIGN KEY(user) REFERENCES users(userName))";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createUserTableQuery);
                statement.execute(createCourseTableQuery);
                statement.execute(createReviewTableQuery);
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

    public static boolean addCourse(String subject, int number, String title){
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String query = "INSERT INTO courses (subject, number, title, rating) VALUES (?, ?, ?, 0.0)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, subject);
                preparedStatement.setInt(2, number);
                preparedStatement.setString(3, title);

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

    public static List<Review> searchReviewBaseOnUser(String user) {
        List<Review> reviews = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM reviews WHERE user = ?");

            try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
                statement.setString(1, user);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Course course = new Course();
                        course.setSubject(resultSet.getString("subject"));
                        course.setNumber(resultSet.getInt("number"));
                        course.setTitle(resultSet.getString("title"));
                        int rating = resultSet.getInt("rating");
                        String comment = resultSet.getString("comment");
                        String time = resultSet.getString("time");
                        Review review = new Review(user, rating, comment, time, course);
                        reviews.add(review);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public static boolean checkCourseExist(String subject, int number, String title) {

        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM courses WHERE subject = ? AND number = ? AND title = ?");
            //System.out.println(queryBuilder.toString());
            try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
                statement.setString(1,subject);
                statement.setInt(2,number);
                statement.setString(3,title);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static List<Review> searchReviewBaseOnCourse(Course course) {
        List<Review> reviews = new ArrayList<>();
        String subject = course.getSubject();
        int number = course.getNumber();
        String title = course.getTitle();
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM reviews WHERE subject = ? AND number = ? and title = ?");

            try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
                statement.setString(1, subject);
                statement.setInt(2, number);
                statement.setString(3, title);


                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String user = resultSet.getString("user");
                        int rating = resultSet.getInt("rating");
                        String comment = resultSet.getString("comment");
                        String time = resultSet.getString("time");
                        Review review = new Review(user, rating, comment, time, course);
                        reviews.add(review);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static boolean alreadyMakeReview(String userName, Course course){
        String subject = course.getSubject();
        int number = course.getNumber();
        String title = course.getTitle();
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String query = "SELECT * FROM reviews WHERE user = ? AND subject = ? AND number = ? AND title = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, subject);
                preparedStatement.setInt(3, number);
                preparedStatement.setString(4, title);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Review getReview(String userName, Course course){
        Review review = null;
        String subject = course.getSubject();
        int number = course.getNumber();
        String title = course.getTitle();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM reviews WHERE user = ? AND subject = ? AND number = ? and title = ?");

            try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
                statement.setString(1, userName);
                statement.setString(2, subject);
                statement.setInt(3, number);
                statement.setString(4, title);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        course.setSubject(resultSet.getString("subject"));
                        course.setNumber(resultSet.getInt("number"));
                        course.setTitle(resultSet.getString("title"));
                        int rating = resultSet.getInt("rating");
                        String comment = resultSet.getString("comment");
                        String time = resultSet.getString("time");
                        review = new Review(userName, rating, comment, time, course);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return review;
    }

    public static double getAverageRating(Course course){
        List<Review> reviews = searchReviewBaseOnCourse(course);
        int total = 0;
        double average = 0.0;
        for (int i = 0; i < reviews.size(); i++){
            total += reviews.get(i).getRating();
        }
        if(reviews.size()!=0) {
            average = (double)total/(double)reviews.size();
        }
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String updateQuery = "UPDATE courses SET rating = ? WHERE subject = ? AND number = ? AND title = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setDouble(1, average);
                preparedStatement.setString(2, course.getSubject());
                preparedStatement.setInt(3, course.getNumber());
                preparedStatement.setString(4, course.getTitle());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Rating updated successfully");
                } else {
                    System.out.println("No matching course found");
                }
            }
            return average;
        } catch (SQLException e) {
            e.printStackTrace();
            return average;
        }
    }

    public static boolean addReview(int rating, String review, Timestamp timestamp, String userName, Course course){
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String insertReviewQuery = "INSERT INTO reviews (rating, user, time, subject, number, title, comment) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertReviewQuery)) {
                String time = timestamp.toString();
                int colonIndex = time.indexOf(":");
                time = time.substring(0,colonIndex+3);
                preparedStatement.setInt(1, rating);
                preparedStatement.setString(2, userName);
                preparedStatement.setString(3, time);
                preparedStatement.setString(4, course.getSubject());
                preparedStatement.setInt(5, course.getNumber());
                preparedStatement.setString(6, course.getTitle());
                preparedStatement.setString(7,review);

                int rowsAffected = preparedStatement.executeUpdate();


                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        //add review
        //also update the rating in course
        //first find how many reviews are there, rating * amount of reviews, then + rating

        //return true;
    }

    public static boolean editReview(int rating, String review, Timestamp timestamp, String user, Course course){
        if (deleteReview(user, course)) {
            return (addReview(rating, review, timestamp, user, course));
        }
        return false;
    }
    public static boolean deleteReview(String user, Course course){
        //also need to update rating in course
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Set parameters for the PreparedStatement
            String deleteQuery = "DELETE FROM reviews WHERE user = ? AND subject = ? AND number = ? AND title = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, user);
                preparedStatement.setString(2, course.getSubject());
                preparedStatement.setInt(3, course.getNumber());
                preparedStatement.setString(4, course.getTitle());

                // Execute the delete query
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Review deleted successfully.");
                    return true;
                } else {
                    System.out.println("No matching item found for deletion.");
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}