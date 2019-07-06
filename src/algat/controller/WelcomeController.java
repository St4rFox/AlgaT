package algat.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML private AnchorPane welcomePane;
    @FXML private Button startButton;
    private Stage stage;

    public void startButtonPressed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/Lessons.fxml"));
            Parent lessonContent = loader.load();
            LessonsController controller = loader.getController();
            controller.setStage(stage);
            stage.getScene().setRoot(lessonContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStage (Stage stage) {
        this.stage = stage;
    }
}
