package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CourseSearchController {
    @FXML
    private TextField subjectSearchBar;
    @FXML
    private TextField numberSearchBar;
    @FXML
    private TextField titleSearchBar;
    @FXML
    private Label nameLabel;
    public void initialize(String userName) {
        nameLabel.setText("Welcome! "+ userName);
    }
}
