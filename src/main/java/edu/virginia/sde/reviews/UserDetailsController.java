package edu.virginia.sde.reviews;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;

public class UserDetailsController {
    UserDatabaseManager userDatabaseManager;
    @FXML
    private Label nameLabel;
    @FXML
    private Label titleLabel;

    private Stage primaryStage;
    private String userName;

    @FXML
    TableView<Review> tableView;
    @FXML
    TableColumn<Review, Double> ratingColumn;
    @FXML
    TableColumn<Review, String> commentColumn;
    @FXML
    TableColumn<Review, String> subjectColumn;
    @FXML
    TableColumn<Review, Integer> numberColumn;
    @FXML
    TableColumn<Review, String> titleColumn;
    @FXML
    TableColumn<Review, String> timeColumn;
    private ObservableList<Review> reviews;
    @FXML
    private AnchorPane anchorPane;


    public void initialize(String userName, Stage primaryStage) {
        this.userName = userName;
        this.primaryStage = primaryStage;
        nameLabel.setText(userName);

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
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Initialize the data list
        reviews = FXCollections.observableArrayList();
        tableView.setItems(reviews);
        tableView.setVisible(false);

        List<Review> reviewList = UserDatabaseManager.searchReviewBaseOnUser(this.userName);
        tableView.setVisible(true);
        reviews.setAll(reviewList);

        titleLabel.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(titleLabel, 0.0);
        anchorPane.setRightAnchor(titleLabel, 0.0);
        titleLabel.setAlignment(Pos.CENTER);

        nameLabel.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(nameLabel, 650.0);
        anchorPane.setRightAnchor(nameLabel, 0.0);
        nameLabel.setAlignment(Pos.CENTER);
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

    public void reviewClicked(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("courseReview.fxml"));
            Parent root = loader.load();

            Review review = tableView.getSelectionModel().getSelectedItem();
            Course course = review.getCourse();
            course.setRating(userDatabaseManager.getAverageRating(course));

            CourseReviewController courseReviewController = loader.getController();
            courseReviewController.initialize(userName, course, primaryStage);


            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle(course.toString());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}