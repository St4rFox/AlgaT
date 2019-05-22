package algat.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import algat.hashtable.Hasher;
import algat.model.Record;
import algat.Config;

public class InitialConfigDialogController implements Initializable {
    // FXML Variables
    @FXML private ChoiceBox<Integer> capacitySelect;
    @FXML private ChoiceBox<Hasher> hashingSelect;
    @FXML private ToggleGroup initialDataOption;
    @FXML private Button nextButton;

    // Instance fields
    private Stage stage;
    private List<Record> data = null;
    private String selectedOption = "customData";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> capacityItems = this.capacitySelect.getItems();
        capacityItems.addAll(Config.TABLE_CAPACITIES);
        capacitySelect.setValue(Config.TABLE_CAPACITIES[0]);

        ObservableList<Hasher> hashingItems = this.hashingSelect.getItems();
        hashingItems.addAll(Hasher.values());
        hashingSelect.setValue(Hasher.values()[0]);

        this.initialDataOption.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            RadioButton selectedToggle = (RadioButton) newToggle.getToggleGroup().getSelectedToggle();
            this.selectedOption = selectedToggle.getId();
            this.nextButton.setText(this.selectedOption.equals("customData") ? "Next" : "Finish");
        });
    }

    public void nextButtonPressed(ActionEvent event) throws IOException {
        if (this.selectedOption.equals("customData")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/UploadDataDialog.fxml"));
            Parent content = loader.load();
            UploadDataDialogController dialogController = loader.getController();
            dialogController.onConfigCompleted(data -> {
                this.data = data;
                this.close();
            });
            this.stage.getScene().setRoot(content);
        } else {
            this.close();
        }
    }

    List<Record> getData() {
        return this.data;
    }

    private void close() {
        int selectedCapacity = capacitySelect.getValue();
        Hasher selectedHasher = hashingSelect.getValue();

        Config.setHashFunction(selectedHasher);
        Config.setTableCapacity(selectedCapacity);

        this.stage.close();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
