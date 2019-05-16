package algat.controller;

import algat.model.Record;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SampleDataDialogController implements Initializable {
    @FXML private ToggleGroup sampleDataOptions;
    @FXML private Button nextButton;
    private SampleDataOption selectedOption = SampleDataOption.RANDOM_DATA;
    private Stage stage;
    private List<Record> data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.sampleDataOptions.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            RadioButton selectedToggle = (RadioButton) newToggle.getToggleGroup().getSelectedToggle();
            String option = selectedToggle.getId();

            switch (option) {
                case "randomData":
                    this.selectedOption = SampleDataOption.RANDOM_DATA;
                    this.nextButton.setText("Finish");
                    break;
                case "customData":
                    this.selectedOption = SampleDataOption.CUSTOM_DATA;
                    this.nextButton.setText("Next");
                    break;
                case "noData":
                    this.selectedOption = SampleDataOption.NO_DATA;
                    this.nextButton.setText("Finish");
                    break;
            }
        });
    }

    public void nextButtonPressed(ActionEvent event) throws IOException {
        if (selectedOption == SampleDataOption.NO_DATA || selectedOption == SampleDataOption.RANDOM_DATA)
            this.stage.close();
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/LoadFromFileDialog.fxml"));
            Parent content = loader.load();
            LoadFromFileDialogController loadFromFileDialogController = loader.getController();
            loadFromFileDialogController.setParentController(this);
            this.stage.getScene().setRoot(content);
        }
    }

    SampleDataOption getSelectedOption() {
        return selectedOption;
    }

    List<Record> getData() {
        return data;
    }

    void closeStage(List<Record> data) {
        this.stage.close();
        this.data = data;
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    enum SampleDataOption {
        RANDOM_DATA,
        CUSTOM_DATA,
        NO_DATA
    }
}
