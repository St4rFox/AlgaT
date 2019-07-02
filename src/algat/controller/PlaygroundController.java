package algat.controller;

import algat.Config;
import algat.lib.ProbeAnimation;
import algat.lib.ScanMethod;
import algat.lib.hashtable.Hasher;
import algat.model.Bucket;
import javafx.animation.Animation;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class PlaygroundController implements Initializable {
    // Toolbar
    @FXML private Slider slider;
    @FXML private Button playButton;

    // Configuration Sidebar
    @FXML private VBox configSideBar;
    @FXML private TextField capacitySelect;
    @FXML private ChoiceBox<Hasher> hasherMenu;
    @FXML private ChoiceBox<ScanMethod> scanMethodMenu;
    @FXML private VBox additionalParams;
    @FXML private VBox stepFieldContainer;
    @FXML private TextField stepField;
    @FXML private VBox secondHasherContainer;
    @FXML private ChoiceBox<Hasher> secondHasherMenu;
    @FXML private Button lockButton;

    // Status Sidebar
    @FXML private Text keyVal;
    @FXML private Text valVal;
    @FXML private Text deletedVal;
    @FXML private Text factorVal;
    @FXML private Text positionVal;

    // Table
    @FXML private HashTableController hashTableController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initUI();
        this.initListeners();

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

                ArrayList<Bucket> initialData = dialogController.getData();
                int capacity = dialogController.getCapacity();
                Hasher hasher = dialogController.getHasher();
                this.capacitySelect.setText(Integer.toString(capacity));
                this.hasherMenu.setValue(hasher);
                this.hashTableController.init(new Config(capacity, hasher), initialData);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        hasherMenu.getItems().addAll(Hasher.values());
        hasherMenu.setValue(Hasher.values()[0]);

        scanMethodMenu.getItems().addAll(ScanMethod.values());
        scanMethodMenu.setValue(ScanMethod.values()[0]);

        stepFieldContainer.managedProperty().bindBidirectional(stepFieldContainer.visibleProperty());
        secondHasherContainer.managedProperty().bindBidirectional(secondHasherContainer.visibleProperty());
        secondHasherContainer.setVisible(false);

        stepField.setText("1");
        secondHasherMenu.getItems().addAll(Hasher.values());
        secondHasherMenu.setValue(Hasher.values()[0]);
    }

    private void initListeners() {
        hashTableController.getAnimation().statusProperty().addListener((observableValue, oldStatus, newStatus) -> {
            ImageView graphic = (ImageView) playButton.getGraphic();

            switch (newStatus) {
                case STOPPED:
                    graphic.setImage(playIcon);
                    break;
                case RUNNING:
                    graphic.setImage(stopIcon);
                    break;
                case PAUSED:
                    graphic.setImage(pauseIcon);
                    break;
            }

            playButton.setGraphic(graphic);
        });

        scanMethodMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldMethod, newMethod) -> {
            stepFieldContainer.setVisible(newMethod == ScanMethod.LINEAR || newMethod == ScanMethod.QUADRATIC);
            secondHasherContainer.setVisible(newMethod == ScanMethod.DOUBLE_HASHING);
            additionalParams.setVisible(newMethod != ScanMethod.RANDOM);
            stepField.setText("1");
        });
    }

    private final Image playIcon = new Image(getClass().getResourceAsStream("/algat/resources/icons/play.png"));
    private final Image pauseIcon = new Image(getClass().getResourceAsStream("/algat/resources/icons/pause.png"));
    private final Image stopIcon = new Image(getClass().getResourceAsStream("/algat/resources/icons/stop.png"));

    // ==================================
    // === LISTENERS ====================
    // ==================================
    public void insertButtonPressed(ActionEvent event) {
        this.prepareForAction();

        ActionDialog dialog = new ActionDialog(
                "Insert Dialog",
                "Insert a new entry into the table",
                true
        );

        dialog.showAndWait().ifPresent(result -> hashTableController.insert(result.getKey(), result.getValue()));
    }

    public void removeButtonPressed(ActionEvent event) {
        this.prepareForAction();

        ActionDialog dialog = new ActionDialog(
                "Remove Dialog",
                "Remove an entry from the table"
        );

        dialog.showAndWait().ifPresent(result -> hashTableController.remove(result.getKey()));
    }

    public void hasKeyButtonPressed(ActionEvent event) {
        this.prepareForAction();

        ActionDialog dialog = new ActionDialog(
                "Has Key Dialog",
                "Check if the table contains a given key"
        );

        dialog.showAndWait().ifPresent(result -> hashTableController.hasKey(result.getKey()));
    }

    public void lockButtonPressed(ActionEvent event) {
        boolean locked = lockButton.getText().equals("Lock");
        lockButton.setText(locked ? "Unlock" : "Lock");

        configSideBar.getChildren().forEach(node -> {
            if (node != lockButton)
                node.setDisable(locked);
        });
    }

    private void prepareForAction() {
        lockButton.fire();
        Config config = createConfig();
        hashTableController.setConfig(config);
    }

    private Config createConfig() {
        int capacity = Integer.parseInt(capacitySelect.getText());
        int step = Integer.parseInt(stepField.getText());

        Config config = new Config();
        config.set(Config.Key.CAPACITY, capacity);
        config.set(Config.Key.HASHER, hasherMenu.getValue());
        config.set(Config.Key.SCAN_METHOD, scanMethodMenu.getValue());
        config.set(Config.Key.STEP, step);
        config.set(Config.Key.SECOND_HASHER, secondHasherMenu.getValue());

        return config;
    }

    public void playButtonPressed(ActionEvent event) {
        ProbeAnimation animation = hashTableController.getAnimation();
        Animation.Status status = animation.getStatus();
        System.out.println(status);

        if (status == Animation.Status.RUNNING)
            animation.stop();
        else
            animation.play();
    }

    public void rewindButtonPressed(ActionEvent event) {
        hashTableController.getAnimation().rewind();
    }

    public void stepBackwardButtonPressed(ActionEvent event) {
        hashTableController.getAnimation().stepBackward();
    }

    public void stepForwardButtonPressed(ActionEvent event) {
        hashTableController.getAnimation().stepForward();
    }

    public void fastForwardButtonPressed(ActionEvent event) {
        hashTableController.getAnimation().play();
    }

//    @Override
//    public void onHashCreated(int hashValue) {
//        System.out.println("Hash value: " + hashValue);
//    }
//
//    @Override
//    public void onScan(int index, HashTableNode node) {
////        this.animation.addNode((BucketComponent) this.tableViewer.getChildren().get(index));
////        this.scanSequence.add(new Pair<>(index, node));
////        modifyStatus(index, node);
//    }
//
//    @Override
//    public void onFinish(int index, HashTableNode selectedNode) {
//        modifyStatus(index, selectedNode);
//
//    }
//
//    private void modifyStatus(int index, HashTableNode selectedNode) {
//
//        if (selectedNode != null && !selectedNode.isDeleted()) {
//            positionVal.setText(String.valueOf(index));
//            keyVal.setText(selectedNode.getKey());
//            valVal.setText(selectedNode.getValue());
//            deletedVal.setText("False");
//        } else {
//            positionVal.setText("NOT FOUND");
//            keyVal.setText("NOT FOUND");
//            valVal.setText("NOT FOUND");
//            deletedVal.setText("True");
//        }
//
//    }
}
