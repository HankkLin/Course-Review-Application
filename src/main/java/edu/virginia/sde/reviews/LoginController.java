package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

// LoginController.java
public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;
    private Stage primaryStage;
    private void setStatus(String message) {
        statusLabel.setText(message);
    }
    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void loginButtonClicked() {
        String userName = usernameField.getText();
        String password = passwordField.getText();

        if (UserDatabaseManager.isValidUser(userName, password)) {
            loadMainScene(userName);
            System.out.println("Login successful");
            setStatus("Login successful");
        } else {
            System.out.println("Invalid username or password");
            setStatus("Invalid username or password");
        }
    }

    public void registerButtonClicked() throws SQLException {
        String userName = usernameField.getText();
        String password = passwordField.getText();

        if (UserDatabaseManager.userExist(userName)) {
            setStatus("User already exists. Registration failed.");
        } else {
            if (password.length() >= 8) {
                if (UserDatabaseManager.addUser(userName, password)) {
                    setStatus("User registered successfully.");
                } else {
                    setStatus("Failed to register user.");
                }
            } else {
                setStatus("Password must be at least 8 characters.");
            }
        }
    }
    private void loadMainScene(String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("courseList.fxml"));
            Parent root = loader.load();

            CourseSearchController courseSearchController = loader.getController();
            courseSearchController.initialize(userName, primaryStage);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Course List Page");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

