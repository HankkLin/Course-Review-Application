package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class CourseSearchController {
    @FXML
    private TextField subjectSearchBar;
    @FXML
    private TextField numberSearchBar;
    @FXML
    private TextField titleSearchBar;
    @FXML
    private Label nameLabel;
    private Stage primaryStage;
    private String userName;
    private Scene mainScene;
    public void initialize(String userName, Stage primaryStage) {
        this.userName = userName;
        this.primaryStage = primaryStage;
        nameLabel.setText(userName);
    }
    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }
    @FXML
    private void showUserDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userprofile.fxml"));
            Parent root = loader.load();

            UserDetailsController userDetailsController = loader.getController();
            userDetailsController.initialize(userName, primaryStage);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("User Details");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
