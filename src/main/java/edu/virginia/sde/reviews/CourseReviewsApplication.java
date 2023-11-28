package edu.virginia.sde.reviews;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Main.java
public class CourseReviewsApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
//        CourseSearchController courseSearchController = loader.getController();
//        Scene mainScene = new Scene(root, 800, 600);
//        courseSearchController.setMainScene(mainScene);

        LoginController loginController = loader.getController();
        loginController.initialize(primaryStage);

        loader = new FXMLLoader(getClass().getResource("courseList.fxml"));
        CourseSearchController courseSearchController = loader.getController();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

