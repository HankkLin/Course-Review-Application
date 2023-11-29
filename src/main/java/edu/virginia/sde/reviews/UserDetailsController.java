package edu.virginia.sde.reviews;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;

public class UserDetailsController {
    @FXML
    private Label nameLabel;

    private Stage primaryStage;
    private String userName;

    @FXML
    TableView<Review> tableView;
    @FXML
    TableColumn<Review, Double> ratingColumn;
    @FXML
    TableColumn<Review, String> commentColumn;
    @FXML
    TableColumn<Review, String> timeColumn;
    private ObservableList<Review> reviews;


    public void initialize(String userName, Stage primaryStage) {
        this.userName = userName;
        this.primaryStage = primaryStage;
        nameLabel.setText("Hello, " + userName + "!");

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Initialize the data list
        reviews = FXCollections.observableArrayList();
        tableView.setItems(reviews);
        tableView.setVisible(false);

        List<Review> reviewList = UserDatabaseManager.searchReviewBaseOnUser(this.userName);
        tableView.setVisible(true);
        reviews.setAll(reviewList);
    }

    @FXML
    private void returnToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("courseList.fxml"));
            Parent root = loader.load();

            CourseSearchController courseSearchController = loader.getController();
            courseSearchController.initialize(userName, primaryStage);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Course List Page");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}