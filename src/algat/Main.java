package algat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent lessons = FXMLLoader.load(getClass().getResource("view/LessonsLayout.fxml"));
        primaryStage.setTitle("AlgaT");
        primaryStage.setScene(new Scene(lessons));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
