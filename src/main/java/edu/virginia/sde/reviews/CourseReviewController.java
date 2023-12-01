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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;

public class CourseReviewController {

    @FXML
    private Label courseLabel;

    private Stage primaryStage;
    private String userName;
    private Course course;
    @FXML
    TableView<Review> tableView;
    @FXML
    TableColumn<Review, Integer> ratingColumn;
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
        this.course = course;
        courseLabel.setText(course.toString());

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        commentColumn.setCellFactory(column -> {
            TableCell<Review, String> cell = new TableCell<>() {
                private final Text text = new Text();
                {
                    text.wrappingWidthProperty().bind(column.widthProperty());
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        setGraphic(text);
                    }
                }
            };
            return cell;
        });
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
            Review review = userDatabaseManager.getReview(userName, course);
            reviewTextField.setText(review.comment);
            ratingSlider.setValue(review.rating);
        } else {
            editReviewButton.setVisible(false);
            deleteReviewButton.setVisible(false);
            addReviewButton.setVisible(true);
            reviewTextField.setText("");
            ratingSlider.setValue(1);

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
        int rating = (int)ratingSlider.getValue();
        String comment = reviewTextField.getText();
        System.out.println(comment);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        userDatabaseManager.addReview(rating, comment, timestamp, this.userName, this.course);
        userDatabaseManager.getAverageRating(this.course);
        initialize(userName,course,primaryStage);
    }
    @FXML
    private void editReview(){
        int rating = (int)ratingSlider.getValue();
        String comment = reviewTextField.getText();
        System.out.println(comment);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        userDatabaseManager.editReview(rating, comment, timestamp, this.userName, this.course);
        userDatabaseManager.getAverageRating(this.course);
        initialize(userName,course,primaryStage);
    }
    @FXML
    private void deleteReview(){
        userDatabaseManager.deleteReview(this.userName, this.course);
        userDatabaseManager.getAverageRating(this.course);
        initialize(this.userName, this.course, this.primaryStage);
        //also need to initialize again
    }


}

