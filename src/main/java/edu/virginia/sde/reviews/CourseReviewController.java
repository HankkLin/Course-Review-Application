package edu.virginia.sde.reviews;
// CourseReviewPage.java
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class CourseReviewController {

    @FXML
    private Label courseLabel;

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
    @FXML
    private Button addReviewButton;
    @FXML
    private Button editReviewButton;
    @FXML
    private Button deleteReviewButton;
    @FXML
    private Slider ratingSlider;
    @FXML
    private TextArea reviewTextField;


    UserDatabaseManager userDatabaseManager = new UserDatabaseManager();


    public void initialize(String userName, Course course, Stage primaryStage) {
        this.userName = userName;
        this.primaryStage = primaryStage;
        courseLabel.setText(course.toString());

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Initialize the data list
        reviews = FXCollections.observableArrayList();
        tableView.setItems(reviews);
        tableView.setVisible(false);

        List<Review> reviewList = UserDatabaseManager.searchReviewBaseOnCourse(course);
        tableView.setVisible(true);
        reviews.setAll(reviewList);
        if(userDatabaseManager.alreadyMakeReview(userName,course)){
            editReviewButton.setVisible(true);
            deleteReviewButton.setVisible(true);
            addReviewButton.setVisible(false);
        } else {
            editReviewButton.setVisible(false);
            deleteReviewButton.setVisible(false);
            addReviewButton.setVisible(true);
        }
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
    @FXML
    private void addReview(){
    }
    @FXML
    private void editReview(){

    }
    @FXML
    private void deleteReview(){

    }
}

