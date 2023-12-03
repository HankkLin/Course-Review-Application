package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;

// LoginController.java
public class LoginController {
    @FXML
    private Label titleLabel;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Button closeButton;
    @FXML
    private ImageView imageView;
    private Stage primaryStage;
    private void setStatus(String message) {
        statusLabel.setText(message);
    }
    public void initialize(Stage primaryStage) {
        double size = 275.0;
        double height = 40.0;
        this.primaryStage = primaryStage;
        File file = new File("src/loginIcon.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);

        statusLabel.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(statusLabel, 0.0);
        anchorPane.setRightAnchor(statusLabel, 0.0);
        statusLabel.setAlignment(Pos.CENTER);

        titleLabel.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(titleLabel, 0.0);
        anchorPane.setRightAnchor(titleLabel, 0.0);
        titleLabel.setAlignment(Pos.CENTER);

        usernameField.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(usernameField, size);
        anchorPane.setRightAnchor(usernameField, size);
        usernameField.setAlignment(Pos.CENTER);
        usernameField.setPrefHeight(height);

        passwordField.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(passwordField, size);
        anchorPane.setRightAnchor(passwordField, size);
        passwordField.setAlignment(Pos.CENTER);
        passwordField.setPrefHeight(height);

        loginButton.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(loginButton, size);
        anchorPane.setRightAnchor(loginButton, size);
        loginButton.setAlignment(Pos.CENTER);
        loginButton.setPrefHeight(height);

        registerButton.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(registerButton, size);
        anchorPane.setRightAnchor(registerButton, size);
        registerButton.setAlignment(Pos.CENTER);
        registerButton.setPrefHeight(height);

        closeButton.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(closeButton, size);
        anchorPane.setRightAnchor(closeButton, size);
        closeButton.setAlignment(Pos.CENTER);
        closeButton.setPrefHeight(height);

        imageView.setPreserveRatio(true);
        imageView.setFitHeight(50);
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
            setStatus("INVALID USERNAME OR PASSWORD");
        }
    }

    public void registerButtonClicked() throws SQLException {
        String userName = usernameField.getText();
        String password = passwordField.getText();

        if (UserDatabaseManager.userExist(userName)) {
            setStatus("USER ALREADY EXISTS. REGISTRATION FAILED.");
        } else {
            if (password.length() >= 8) {
                if (UserDatabaseManager.addUser(userName, password)) {
                    setStatus("User registered successfully.");
                } else {
                    setStatus("Failed to register user.");
                }
            } else {
                setStatus("PASSWORD MUST BE AT LEAST 8 CHARACTERS");
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
            scene.getStylesheets().add(getClass().getResource("courseList.css").toExternalForm());
            primaryStage.setTitle("Course List Page");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeFunction(){
        primaryStage.close();
    }
}

