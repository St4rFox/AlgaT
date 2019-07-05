package algat;

import algat.controller.LessonsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Lessons.fxml"));
        Parent lessonContent = loader.load();
        LessonsController controller = loader.getController(); //torna il controller specificato nel file FXML
        primaryStage.setMaximized(true);
        controller.setStage(primaryStage);
        primaryStage.setTitle("AlgaT");
        primaryStage.setScene(new Scene(lessonContent));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
