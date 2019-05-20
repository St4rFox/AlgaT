package algat.controller;

import algat.Config;
import algat.hashtable.HashTable;
import algat.hashtable.Hasher;
import algat.model.Record;
import javafx.application.Platform;
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
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class PlaygroundController implements Initializable, HashTableDelegate {
    // FXML Fields
    @FXML private Slider slider;
    @FXML private VBox tableViewer;

    // Instance fields
    private HashTable table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
