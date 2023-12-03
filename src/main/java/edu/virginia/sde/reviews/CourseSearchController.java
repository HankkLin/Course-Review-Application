package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.List;


public class CourseSearchController {
    @FXML
    private TextField subjectSearchBar;
    @FXML
    private TextField numberSearchBar;
    @FXML
    private TextField titleSearchBar;
    @FXML
    private Label nameLabel;
    @FXML Label searchLabel = new Label();
    private Stage primaryStage;
    private String userName;
    private Scene mainScene;
    @FXML
    TableView<Course> tableView;
    @FXML
    TableColumn<Course, String> subjectColumn;
    @FXML
    TableColumn<Course, Integer> numberColumn;
    @FXML
    TableColumn<Course, String> titleColumn;
    @FXML
    TableColumn<Course, Double> avgScoreColumn;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Button logoutButton;

    private ObservableList<Course> courseData;
    public void initialize(String userName, Stage primaryStage) {
        double height = 40.0;
        this.userName = userName;
        this.primaryStage = primaryStage;
        nameLabel.setText(userName);
        searchLabel.setText("Type in different info to search");

        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        avgScoreColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Initialize the data list
        courseData = FXCollections.observableArrayList();
        tableView.setItems(courseData);
        tableView.setVisible(false);

        titleLabel.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(titleLabel, 0.0);
        anchorPane.setRightAnchor(titleLabel, 0.0);
        titleLabel.setAlignment(Pos.CENTER);

        searchLabel.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(searchLabel, 0.0);
        anchorPane.setRightAnchor(searchLabel, 0.0);
        searchLabel.setAlignment(Pos.CENTER);

        nameLabel.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(nameLabel, 650.0);
        anchorPane.setRightAnchor(nameLabel, 0.0);
        nameLabel.setAlignment(Pos.CENTER);

        logoutButton.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setLeftAnchor(logoutButton, 670.0);
        anchorPane.setRightAnchor(logoutButton, 20.0);
        logoutButton.setAlignment(Pos.CENTER);


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
    public void searchButtonClicked() {
        String subject = subjectSearchBar.getText();
        String numberString = numberSearchBar.getText();
        int number;
        if (numberString.equals("")){
            number = 0;
        } else {
            try {
                number = Integer.parseInt(numberString);
                if (number < 1 || number > 9999) {
                    throw new NumberFormatException("Number must be a positive integer with at most 4 digits");
                }
            } catch (NumberFormatException e) {
                searchLabel.setText("Error in Course Number: " + e.getMessage());
                searchLabel.setVisible(true);
                tableView.setVisible(false);
                searchLabel.setStyle(" -fx-text-fill: #e55c22; -fx-font-family: 'OCR A Extended'; -fx-font-size: 15px;");
                return;
            }
        }
        String title = titleSearchBar.getText();

        List<Course> courses = UserDatabaseManager.searchCourses(subject, number, title);
        if (courses.isEmpty()){
            searchLabel.setStyle(" -fx-text-fill: #372d22; -fx-font-family: 'OCR A Extended'; -fx-font-size: 15px;");
            searchLabel.setText("No course found, try soemthing else.");
            searchLabel.setVisible(true);
            tableView.setVisible(false);
            return;
        }
        tableView.setVisible(true);
        searchLabel.setVisible(false);
        courseData.setAll(courses);

        //Help function that print out the result as a string
//        StringBuilder result = new StringBuilder();
//        for (Course course : courses) {
//            result.append(course.toString()).append("\n");
//        }
//        searchLabel.setText(result.toString());

    }
    public void addButtonClicked(){
        String subject = subjectSearchBar.getText();
        String numberString = numberSearchBar.getText();
        int number;
        if (numberString.equals("")){
            searchLabel.setStyle(" -fx-text-fill: #e55c22; -fx-font-family: 'OCR A Extended'; -fx-font-size: 15px;");
            searchLabel.setText("Can't add a course with empty course number!");
            searchLabel.setVisible(true);
            tableView.setVisible(false);
            return;
        } else {
            try {
                number = Integer.parseInt(numberString);
                if (number < 1 || number > 9999) {
                    searchLabel.setStyle(" -fx-text-fill: #e55c22; -fx-font-family: 'OCR A Extended'; -fx-font-size: 15px;");
                    throw new NumberFormatException("Number must be between 1 to 9999.");
                }
            } catch (NumberFormatException e) {
                searchLabel.setText("Error in Course Number: " + e.getMessage());
                searchLabel.setVisible(true);
                tableView.setVisible(false);
                return;
            }
        }
        String title = titleSearchBar.getText();
        if(subject.equals("")){
            searchLabel.setStyle(" -fx-text-fill: #e55c22; -fx-font-family: 'OCR A Extended'; -fx-font-size: 15px;");
            searchLabel.setText("Can't add a course with empty subject!");
            searchLabel.setVisible(true);
            tableView.setVisible(false);
            return;
        }
        if(title.equals("")){
            searchLabel.setStyle(" -fx-text-fill: #e55c22; -fx-font-family: 'OCR A Extended'; -fx-font-size: 15px;");
            searchLabel.setText("Can't add a course with empty title!");
            searchLabel.setVisible(true);
            tableView.setVisible(false);
            return;
        }
        if(UserDatabaseManager.checkCourseExist(subject,number,title)){
            Course newCourse = new Course(subject,number,title);
            searchLabel.setText("Course already exist: " + newCourse.toString());
            searchLabel.setVisible(true);
            tableView.setVisible(false);
            return;
        }
        Course newCourse = new Course(subject,number,title);
        if(UserDatabaseManager.addCourse(subject,number,title)) {
            searchLabel.setStyle(" -fx-text-fill: #372d22; -fx-font-family: 'OCR A Extended'; -fx-font-size: 15px;");
            searchLabel.setText("Add course: " + newCourse.toString());
            searchLabel.setVisible(true);
            tableView.setVisible(false);
            return;
        }
        else{

        }

    }
    public void logoutButtonClicked(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.initialize(primaryStage);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("User Details");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void courseClicked(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("courseReview.fxml"));
            Parent root = loader.load();

            Course course = tableView.getSelectionModel().getSelectedItem();

            CourseReviewController courseReviewController = loader.getController();
            courseReviewController.initialize(userName, course, primaryStage);


            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle(course.toString());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            tableView.setVisible(false);
            searchLabel.setVisible(true);
            searchLabel.setText("Don't click on empty column!!");
            e.printStackTrace();
        }
    }
}
