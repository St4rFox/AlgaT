package algat.controller;

import algat.lib.hashtable.Hasher;
import algat.model.Bucket;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InitialConfigDialogController implements Initializable {
    // FXML Variables
    @FXML private TextField capacityField;
    @FXML private ChoiceBox<Hasher> hashingSelect;
    @FXML private ToggleGroup initialDataOption;
    @FXML private Button nextButton;

    // Instance fields
    private Stage stage;
    private ArrayList<Bucket> data = null;
    private String selectedOption = "noData";
    private int capacity;
    private Hasher hasher;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Hasher> hashingItems = this.hashingSelect.getItems();
        hashingItems.addAll(Hasher.values());
        hashingSelect.setValue(Hasher.values()[0]);
        this.initListeners();
    }

    private void initListeners() {
        this.initialDataOption.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            RadioButton selectedToggle = (RadioButton) newToggle.getToggleGroup().getSelectedToggle();
            this.selectedOption = selectedToggle.getId();
            this.nextButton.setText(this.selectedOption.equals("customData") ? "Next" : "Finish");
        });
    }

    public void nextButtonPressed(ActionEvent event) throws IOException {
        if (capacityIsValid()) {
            if (this.selectedOption.equals("customData")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/UploadDataDialog.fxml"));
                Parent content = loader.load();
                UploadDataDialogController dialogController = loader.getController();
                dialogController.onConfigCompleted(data -> {
                    this.data = data;
                    this.stage.close();
                });
                this.stage.getScene().setRoot(content);
            } else {
                this.stage.close();
            }
        }
    }

    ArrayList<Bucket> getData() {
        return this.data;
    }

    int getCapacity() { return capacity; }

    Hasher getHasher() { return hashingSelect.getValue(); }

    private boolean capacityIsValid() {
        try {
            capacity = Integer.parseInt(capacityField.getText());
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
