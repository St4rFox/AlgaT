package algat.controller;

import algat.Config;
import algat.ScanMethod;
import algat.hashtable.Hasher;
import algat.hashtable.HashTable;

import com.sun.source.doctree.TextTree;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlaygroundController implements Initializable {
    // FXML Fields
    @FXML private Slider slider;
    @FXML private StackPane viewer;
    @FXML private ViewerController viewerController;
    @FXML private TextField capacitySelect;
    @FXML private ChoiceBox<Hasher> hasherSelect;
    @FXML private ChoiceBox<ScanMethod> scannerSelect;
    @FXML private TextArea keyVal;
    @FXML private TextArea valVal;
    @FXML private TextArea deletedVal;
    @FXML private TextArea factorVal;
    @FXML private TextArea positionVal;



    // Instance fields
    private HashTable table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        keyVal.setEditable(false);
        valVal.setEditable(false);
        deletedVal.setEditable(false);
        factorVal.setEditable(false);
        positionVal.setEditable(false);

        capacitySelect.setOnAction((clicked) -> {
            int newCapacity = 0;
                try{
                    newCapacity = Integer.parseInt(capacitySelect.getCharacters().toString());
                } catch (NumberFormatException e) {
                    capacitySelect.textProperty().setValue("0");
                }
                Config.setTableCapacity(newCapacity);
                System.out.println(newCapacity);
        });

        ObservableList<ScanMethod> scanItems = scannerSelect.getItems();
        scanItems.addAll(ScanMethod.values());
        scannerSelect.setValue(ScanMethod.values()[0]);
        scannerSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, oldScanner, newScanner) -> {
            Config.setScanMethod(newScanner);
        });

        ObservableList<Hasher> hashItems = hasherSelect.getItems();
        hashItems.addAll(Hasher.values());
        hasherSelect.setValue(Hasher.values()[0]);
        hasherSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, oldHash, newHash) -> {
            Config.setHashFunction(newHash);
        });

        try {
            FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/algat/view/InitialConfigDialog.fxml"));
            Parent content = dialogLoader.load();

            final Scene scene = new Scene(content);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);

            InitialConfigDialogController dialogController = dialogLoader.getController();
            dialogController.setStage(stage);

            Platform.runLater(() -> {
                stage.showAndWait();
                this.viewerController.initTable(dialogController.getData());
                this.viewerController.render();
                capacitySelect.textProperty().setValue(Integer.toString(Config.getTableCapacity()));
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
//        Dialog<Pair<String, String>> newEntryDialog = new Dialog<>();
//        newEntryDialog.setTitle("New Entry");
//        newEntryDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
//
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 150, 10, 10));
//
//        TextField key = new TextField();
//        key.setPromptText("Key");
//        TextField value = new TextField();
//        value.setPromptText("Value");
//
//        grid.add(new Label("Key"), 0, 0);
//        grid.add(key, 1, 0);
//        grid.add(new Label("Value"), 0, 1);
//        grid.add(value, 1, 1);
//
//        newEntryDialog.getDialogPane().setContent(grid);
//
//        newEntryDialog.setResultConverter(button -> {
//            if (button == ButtonType.OK) {
//                return new Pair<>(key.getText(), value.getText());
//            }
//            return null;
//        });
//
//        Optional<Pair<String, String>> result = newEntryDialog.showAndWait();
//
//        result.ifPresent(entry -> {
//            viewerController.onInsert(entry.getKey(), entry.getValue());
//        });
    }

    public void findButtonPressed(ActionEvent event) {

    }

    public void uploadButtonPressed(ActionEvent event) {

    }
}
