package algat.controller;

import algat.Config;
import algat.lib.ScanAnimation;
import algat.lib.ScanMethods;
import algat.lib.hashtable.HashTable;
import algat.lib.hashtable.HashTableNode;
import algat.lib.hashtable.Hasher;
import algat.lib.scanmethods.DoubleHashingScanMethod;
import algat.lib.scanmethods.LinearScanMethod;
import algat.lib.scanmethods.QuadraticScanMethod;
import algat.lib.scanmethods.ScanMethod;
import algat.model.Record;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class PlaygroundController implements Initializable, HashTableDelegate {
    // Toolbar
    @FXML private Slider slider;

    // Configuration Sidebar
    @FXML private VBox configSideBar;
    @FXML private TextField capacitySelect;
    @FXML private ChoiceBox<Hasher> hasherMenu;
    @FXML private ChoiceBox<ScanMethods.Names> scanMethodMenu;
    @FXML private VBox additionalParams;
    @FXML private VBox stepFieldContainer;
    @FXML private TextField stepField;
    @FXML private VBox secondHasherContainer;
    @FXML private ChoiceBox<Hasher> secondHasherMenu;

    // Status Sidebar
    @FXML private Text keyVal;
    @FXML private Text valVal;
    @FXML private Text deletedVal;
    @FXML private Text factorVal;
    @FXML private Text positionVal;

    // Others
    @FXML private VBox tableViewer;

    // Instance fields
    private HashTable table;
    private ArrayList<Pair<Integer, HashTableNode>> scanSequence = new ArrayList<>();
    private int cursor = -1;
    private ScanAnimation animation = new ScanAnimation();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initUI();
        this.initListeners();
        positionVal.setTextAlignment(TextAlignment.CENTER);

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
                capacitySelect.textProperty().setValue(Integer.toString(Config.getCapacity()));
                this.initTable(dialogController.getData());
                this.initViewer();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        hasherMenu.getItems().addAll(Hasher.values());
        hasherMenu.setValue(Hasher.values()[0]);

        scanMethodMenu.getItems().addAll(ScanMethods.Names.values());
        scanMethodMenu.setValue(ScanMethods.Names.values()[0]);

        stepField.setText("1");

        secondHasherMenu.getItems().addAll(Hasher.values());
        secondHasherMenu.setValue(Hasher.values()[0]);

        stepFieldContainer.managedProperty().bindBidirectional(stepFieldContainer.visibleProperty());
        secondHasherContainer.managedProperty().bindBidirectional(secondHasherContainer.visibleProperty());
        secondHasherContainer.setVisible(false);

        Button lockButton = new Button("Lock");

        lockButton.setOnAction(actionEvent -> {
            Boolean locked = lockButton.getText().equals("Lock");
            lockButton.setText(locked ? "Unlock" : "Lock");
            setLocked(locked);
        });

        lockButton.setId("lockButton");
        configSideBar.getChildren().add(lockButton);
    }

    private void setLocked(Boolean locked){
        configSideBar.getChildren().forEach(node -> {
            String id = node.getId();
            if (id == null || !id.equals("lockButton")) node.setDisable(locked);
        });
    }

    private void initListeners() {

        scanMethodMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldMethod, newMethod) -> {
            stepFieldContainer.setVisible(newMethod == ScanMethods.Names.LINEAR || newMethod == ScanMethods.Names.QUADRATIC);
            secondHasherContainer.setVisible(newMethod == ScanMethods.Names.DOUBLE_HASHING);
            additionalParams.setVisible(newMethod != ScanMethods.Names.RANDOM);
            stepField.setText("1");
        });

    }

    private void initTable(List<Record> data) {
        Hasher hashFunction = Config.getHasher();

        if (data != null) {
            Config.setCapacity(data.size());
            this.table = new HashTable(data.size(), hashFunction);

            for (Record record : data) {
                this.table.put(record.getKey(), record.getValue());
            }
        } else {
            this.table = new HashTable(Config.getCapacity(), hashFunction);
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
        modifyStatus(index, node);
    }

    @Override
    public void onFinish(int index, HashTableNode selectedNode) {
        modifyStatus(index, selectedNode);

    }

    private void modifyStatus(int index, HashTableNode selectedNode) {

        if (selectedNode != null && !selectedNode.isDeleted()) {
            positionVal.setText(String.valueOf(index));
            keyVal.setText(selectedNode.getKey());
            valVal.setText(selectedNode.getValue());
            deletedVal.setText("False");
        } else {
            positionVal.setText("NOT FOUND");
            keyVal.setText("NOT FOUND");
            valVal.setText("NOT FOUND");
            deletedVal.setText("True");
        }

    }

    // ==================================
    // === LISTENERS ====================
    // ==================================
    public void insertButtonPressed(ActionEvent event) {
        Button button = (Button)configSideBar.lookup("#lockButton");
        button.fire();
        ActionDialog dialog = new ActionDialog(
                "Insert Dialog",
                "Insert a new entry into the table",
                true
        );

        dialog.showAndWait().ifPresent(System.out::println);
    }

    public void removeButtonPressed(ActionEvent event) {
        Button button = (Button)configSideBar.lookup("#lockButton");
        button.fire();
        ActionDialog dialog = new ActionDialog(
                "Remove Dialog",
                "Remove an entry from the table"
        );

        dialog.showAndWait().ifPresent(System.out::println);
    }

    public void findButtonPressed(ActionEvent event) {
        Button button = (Button)configSideBar.lookup("#lockButton");
        button.fire();
        ActionDialog dialog = new ActionDialog(
                "Contains Dialog",
                "Check if the table contains a given key"
        );

        dialog.showAndWait().ifPresent(System.out::println);
    }

    private void createConfig(){
        Config.setCapacity(Integer.parseInt(capacitySelect.getText()));
        //Config.setScanMethod(scanMethodMenu.getValue());
        Config.setHasher(hasherMenu.getValue());
    }

    //TODO: Better implement animation
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
}
