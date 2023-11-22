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
    public void setPrimaryStage(Stage primaryStage) {
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
            System.out.println("User already exists. Registration failed.");
            setStatus("User already exists. Registration failed.");
        } else {
            if (UserDatabaseManager.addUser(userName, password)) {
                System.out.println("User registered successfully.");
                setStatus("User registered successfully.");
            } else {
                System.out.println("Failed to register user.");
                setStatus("Failed to register user.");
            }
        }
    }
    private void loadMainScene(String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("courseList.fxml"));
            Parent root = loader.load();

            CourseSearchController courseSearchController = loader.getController();
            courseSearchController.initialize(userName);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Course List Page");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

