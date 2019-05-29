package algat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent playground = FXMLLoader.load(getClass().getResource("view/Playground.fxml"));
        primaryStage.setTitle("AlgaT");
        primaryStage.setScene(new Scene(playground));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
