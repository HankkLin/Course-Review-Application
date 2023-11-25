package edu.virginia.sde.reviews;
// CourseReviewPage.java
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CourseReviewController {

    @FXML
    private Label titleLabel;
    @FXML
    private Button backButton;

    public void initialize(String courseTitle, Runnable onBack) {
        titleLabel.setText(courseTitle);
        backButton.setOnAction(event -> onBack.run());
    }
}

