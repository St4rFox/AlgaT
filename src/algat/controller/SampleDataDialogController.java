package algat.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SampleDataDialogController implements Initializable {
    @FXML private ToggleGroup sampleDataOptions;
    @FXML private RadioButton randomData;
    @FXML private RadioButton customData;
    @FXML private RadioButton noData;
    @FXML private Button nextButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.sampleDataOptions.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            RadioButton selectedToggle = (RadioButton) newToggle.getToggleGroup().getSelectedToggle();
            String option = selectedToggle.getId();

            if (option.equals("noData"))
                nextButton.setText("Finish");
            else
                nextButton.setText("Next");
        });
    }

    public void nextButtonPressed(ActionEvent event) {
        Stage stage = (Stage) nextButton.getScene().getWindow();
        stage.close();
    }

    private enum SampleDataOption {
        RANDOM_DATA,
        CUSTOM_DATA,
        NO_DATA
    }
}
