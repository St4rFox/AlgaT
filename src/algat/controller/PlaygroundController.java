package algat.controller;

import algat.Config;
import algat.ScanMethod;
import algat.hashtable.Hasher;
import algat.hashtable.HashTable;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class PlaygroundController implements Initializable, HashTableDelegate {
    // FXML Fields
    @FXML private Slider slider;
    @FXML private StackPane viewer;
    @FXML private TextField capacitySelect;
    @FXML private ChoiceBox<Hasher> hasherSelect;
    @FXML private ChoiceBox<ScanMethod> scannerSelect;
    @FXML private TextArea keyVal;
    @FXML private TextArea valVal;
    @FXML private TextArea deletedVal;
    @FXML private TextArea factorVal;
    @FXML private TextArea positionVal;
    @FXML private VBox tableViewer;

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
                capacitySelect.textProperty().setValue(Integer.toString(Config.getTableCapacity()));
                this.initTable(dialogController.getData());
                this.initViewer();
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

    }

    public void findButtonPressed(ActionEvent event) {
        Dialog<String> newEntryDialog = new Dialog<>();
        newEntryDialog.setTitle("New Entry");
        newEntryDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField keyField = new TextField();
        keyField.setPromptText("Key");

        grid.add(new Label("Key"), 0, 0);
        grid.add(keyField, 1, 0);

        newEntryDialog.getDialogPane().setContent(grid);

        newEntryDialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return keyField.getText();
            }
            return null;
        });

        newEntryDialog.showAndWait().ifPresent(key -> {
            System.out.println(this.table.contains(key));
        });
    }

    public void uploadButtonPressed(ActionEvent event) {

    }

    private void initTable(List<Record> data) {
        Hasher hashFunction = Config.getHashFunction();

        if (data != null) {
            Config.setTableCapacity(data.size());
            this.table = new HashTable(Config.getTableCapacity(), hashFunction);

            for (Record record : data) {
                this.table.put(record.getKey(), record.getValue());
            }
        } else {
            this.table = new HashTable(Config.getTableCapacity(), hashFunction);
        }
    }

    private void initViewer() {
        for (HashTable.HashTableNode node : this.table) {
            TableNode tableNode = new TableNode(node.getKey(), node.getValue());
            this.tableViewer.getChildren().add(tableNode);
        }

        this.table.delegate = this;
    }

    @Override
    public void onHashComputation(int hashValue) {
        System.out.println("Hash value: " + hashValue);
    }

    @Override
    public void onNodeInspection(int index, HashTable.HashTableNode node) {
        System.out.println("Index: " + index);
        System.out.println("Node Inspected: " + node);

        TableNode inspected = (TableNode) this.tableViewer.getChildren().get(index);
        inspected.getStyleClass().add("active");
    }
}
