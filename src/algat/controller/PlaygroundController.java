package algat.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlaygroundController implements Initializable {
    // FXML Fields
    @FXML private Slider slider;
    @FXML private AnchorPane viewer;
    @FXML private ViewerController viewerController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void stepBackwardButtonPressed(ActionEvent event) {

    }

    public void stepForwardButtonPressed(ActionEvent event) {

    }

    public void fastBackwardButtonPressed(ActionEvent event) {

    }

    public void fastForwardButtonPressed(ActionEvent event) {

    }

    public void removeButtonPressed(ActionEvent event) {

    }

    public void insertButtonPressed(ActionEvent event) {
        Dialog<Pair<String, String>> newEntryDialog = new Dialog<>();
        newEntryDialog.setTitle("New Entry");
        newEntryDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField key = new TextField();
        key.setPromptText("Key");
        TextField value = new TextField();
        value.setPromptText("Value");

        grid.add(new Label("Key"), 0, 0);
        grid.add(key, 1, 0);
        grid.add(new Label("Value"), 0, 1);
        grid.add(value, 1, 1);

        newEntryDialog.getDialogPane().setContent(grid);

        newEntryDialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Pair<>(key.getText(), value.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = newEntryDialog.showAndWait();

        result.ifPresent(entry -> {
            System.out.println("Username=" + entry.getKey() + ", Password=" + entry.getValue());
        });
    }

    public void findButtonPressed(ActionEvent event) {

    }

    public void uploadButtonPressed(ActionEvent event) {

    }
}
