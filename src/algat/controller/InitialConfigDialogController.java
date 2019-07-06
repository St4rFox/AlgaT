package algat.controller;

import algat.Config;
import algat.lib.ScanMethod;
import algat.lib.hashtable.Hasher;
import algat.model.Bucket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InitialConfigDialogController implements Initializable {
    // FXML Variables
    @FXML private TextField capacityField;
    @FXML private ChoiceBox<Hasher> hashingSelect;
    @FXML private ChoiceBox<ScanMethod> scanMethodSelect;
    @FXML private ToggleGroup initialDataOption;
    @FXML private Button nextButton;

    @FXML private VBox additionalParams;
    @FXML private TextField stepField;
    @FXML private ChoiceBox<Hasher> secondHasher;

    // Instance fields
    private Stage stage;
    private ArrayList<Bucket> data = null;
    private String selectedOption = "noData";
    private int capacity;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Hasher> hashers = FXCollections.observableArrayList(Hasher.values());
        hashingSelect.setItems(hashers);
        hashingSelect.setValue(Hasher.NAIVE);
        secondHasher.setItems(hashers);
        secondHasher.setValue(Hasher.NAIVE);

        scanMethodSelect.setItems(FXCollections.observableArrayList(ScanMethod.values()));
        scanMethodSelect.setValue(ScanMethod.LINEAR);

        additionalParams.managedProperty().bind(additionalParams.visibleProperty());
        stepField.managedProperty().bind(stepField.visibleProperty());
        secondHasher.managedProperty().bind(secondHasher.visibleProperty());
        secondHasher.setVisible(false);

        this.initListeners();
    }

    private void initListeners() {
        scanMethodSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            additionalParams.setVisible(newValue != ScanMethod.RANDOM);
            stepField.setVisible(newValue == ScanMethod.LINEAR || newValue == ScanMethod.QUADRATIC);
            secondHasher.setVisible(newValue == ScanMethod.DOUBLE_HASHING);
        });

        this.initialDataOption.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            RadioButton selectedToggle = (RadioButton) newToggle.getToggleGroup().getSelectedToggle();
            this.selectedOption = selectedToggle.getId();
            this.nextButton.setText(this.selectedOption.equals("customData") ? "Next" : "Finish");
        });
    }

    public void nextButtonPressed(ActionEvent event) throws IOException {
        if (isCapacityValid()) {
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

    Config getInitialConfig() {
        Config config = new Config(capacity, hashingSelect.getValue(), scanMethodSelect.getValue());
        config.set(Config.Key.STEP, Integer.parseInt(stepField.getText()));
        config.set(Config.Key.SECOND_HASHER, secondHasher.getValue());
        return config;
    }

    private boolean isCapacityValid() {
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
