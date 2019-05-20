package algat.controller;

import algat.Config;
import algat.ScanMethod;
import algat.hashtable.Hasher;
import algat.model.Record;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

import algat.controller.InitialConfigDialogController.SampleDataOption;

public class PlaygroundController implements Initializable {
    // FXML Fields
    @FXML private Slider slider;
    @FXML private ViewerController viewerController;
    @FXML private TextField capacitySelect;
    @FXML private ChoiceBox<Hasher> hasherSelect;
    @FXML private ChoiceBox<ScanMethod> scannerSelect;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Hasher> items = hasherSelect.getItems();
        items.addAll(Hasher.values());
        hasherSelect.setValue(Hasher.values()[0]);
        hasherSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, oldHasher, newHasher) -> {
            Config.setHashFunction(newHasher);
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/InitialConfigDialog.fxml"));
            Parent content = loader.load();
            final Scene scene = new Scene(content);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);

            InitialConfigDialogController dialogController = loader.getController();
            dialogController.setStage(stage);

            Platform.runLater(() -> {
                stage.showAndWait();

                dialogController.getData().ifPresent(data -> {
                    data.forEach(System.out::println);
                });

                System.out.println(Config.get());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        newEntryDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

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
            viewerController.onInsert(entry.getKey(), entry.getValue());
        });
    }

    public void findButtonPressed(ActionEvent event) {

    }

    public void uploadButtonPressed(ActionEvent event) {

    }

    private void initTable(LinkedList<Record> data) {

    }
}
