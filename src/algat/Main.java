package algat;

import algat.controller.WelcomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Welcome.fxml"));
        Parent welcomeContent = loader.load();
        WelcomeController controller = loader.getController(); //torna il controller specificato nel file FXML
        primaryStage.setMaximized(true);
        controller.setStage(primaryStage);
        primaryStage.setTitle("AlgaT");
        primaryStage.setScene(new Scene(welcomeContent));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
