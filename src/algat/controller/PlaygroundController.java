package algat.controller;

import algat.Config;
import algat.lib.ScanAnimation;
import algat.lib.hashtable.Hasher;
import algat.lib.hashtable.HashTable;
import algat.lib.hashtable.HashTableNode;
import algat.lib.scanmethods.*;
import algat.model.Record;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class PlaygroundController implements Initializable, HashTableDelegate {
    // FXML Fields
    @FXML private Slider slider;
    @FXML private TextField capacitySelect;
    @FXML private ChoiceBox<Hasher> hasherSelect;
    @FXML private ChoiceBox<ScanMethod> scannerSelect;
    @FXML private Text keyVal;
    @FXML private Text valVal;
    @FXML private Text deletedVal;
    @FXML private Text factorVal;
    @FXML private Text positionVal;
    @FXML private VBox tableViewer;
    @FXML private VBox configurationTab;

    // Instance fields
    private HashTable table;
    private ArrayList<Pair<Integer, HashTableNode>> scanSequence = new ArrayList<>();
    private int cursor = -1;
    private ScanAnimation animation = new ScanAnimation();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        capacitySelect.setOnAction((clicked) -> {
            int newCapacity = 0;

            try {
                newCapacity = Integer.parseInt(capacitySelect.getCharacters().toString());
            } catch (NumberFormatException e) {
                capacitySelect.textProperty().setValue("0");
            }

            Config.setTableCapacity(newCapacity);
        });

        ObservableList<ScanMethod> scanItems = scannerSelect.getItems();
        scanItems.addAll(new LinearScanMethod(), new QuadraticScanMethod(), new RandomScanMethod(), new DoubleHashScanMethod());
        scannerSelect.setValue(scannerSelect.getItems().get(2));

        TextField step = new TextField();
        step.setPromptText("Step");

        ChoiceBox<Hasher> hasher = new ChoiceBox<>();
        hasher.getItems().addAll(Hasher.values());
        hasherSelect.setValue(Hasher.values()[0]);

        configurationTab.getChildren().add(step);
        configurationTab.getChildren().add(hasher);
        hasher.setVisible(false);
        step.setVisible(false);

        scannerSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, oldScanner, newScanner) -> {
            Config.setScanMethod(newScanner);
            switch (newScanner.toString()){

                case "Scansione Lineare":
                    step.setOnAction((clicked) -> {
                        int stepInserted = 1;
                        try {
                            stepInserted = Integer.parseInt(step.getCharacters().toString());
                        } catch(NumberFormatException e) {
                            step.textProperty().setValue("1");
                        }
                        ((LinearScanMethod)newScanner).setStep(stepInserted);

                    });
                    break;

                case "Scansione Quadratica":
                    step.setOnAction((clicked) -> {
                        int stepInserted = 1;
                        try {
                            stepInserted = Integer.parseInt(step.getCharacters().toString());
                        } catch(NumberFormatException e) {
                            step.textProperty().setValue("1");
                        }
                        ((QuadraticScanMethod)newScanner).setStep(stepInserted);

                    });
                    break;

                case "Scasione PseudoCasuale":
                    break;

                case "Hashing Doppio":
                    hasherSelect.getSelectionModel().selectedItemProperty().addListener((obsVal, oldHash, newHash) -> {
                        ((DoubleHashScanMethod)newScanner).setHasher(newHash);
                    });
                    break;
            }
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
        for (HashTableNode node : this.table) {
            TableNode tableNode = new TableNode(node.getKey(), node.getValue());
            this.tableViewer.getChildren().add(tableNode);
        }

        this.table.delegate = this;
    }

    @Override
    public void onHashCreated(int hashValue) {
        System.out.println("Hash value: " + hashValue);
    }

    @Override
    public void onScan(int index, HashTableNode node) {
        this.animation.addNode((TableNode) this.tableViewer.getChildren().get(index));
        this.scanSequence.add(new Pair<>(index, node));
    }

    @Override
    public void onFinish(int index, HashTableNode selectedNode) {

    }

    public void stepBackwardButtonPressed(ActionEvent event) {
        animation.stepBackward();
    }

    public void stepForwardButtonPressed(ActionEvent event) {
        animation.stepForward();
    }

    public void fastBackwardButtonPressed(ActionEvent event) {
        this.animation.setRate(-1.0);
        this.animation.play();
    }

    public void fastForwardButtonPressed(ActionEvent event) {
        this.animation.setRate(1.0);
        this.animation.play();
    }

    public void removeButtonPressed(ActionEvent event) {

    }

    public void insertButtonPressed(ActionEvent event) {

    }
}
