package algat.controller;

import algat.Config;
import algat.lib.ErrorCodes;
import algat.lib.ProbeAnimation;
import algat.lib.ScanMethod;
import algat.lib.exceptions.NoSuchKeyException;
import algat.lib.hashtable.Hasher;
import algat.model.Bucket;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class PlaygroundController implements Initializable {
    // Toolbar
    @FXML private Button playButton;
    @FXML private CheckBox animationsSettings;
    @FXML private RadioButton radioAnimation;

    @FXML private TextArea errorMessages;

    // Configuration Sidebar
    @FXML private VBox configSideBar;
    @FXML private Slider capacityBar;
    @FXML private ChoiceBox<Hasher> hasherMenu;
    @FXML private ChoiceBox<ScanMethod> scanMethodMenu;
    @FXML private VBox additionalParams;
    @FXML private VBox stepFieldContainer;
    @FXML private Slider stepBar;
    @FXML private VBox secondHasherContainer;
    @FXML private ChoiceBox<Hasher> secondHasherMenu;
    @FXML private Button lockButton;

    // Table
    @FXML private HashTableController hashTableController;

    private Stage stage;

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
                Config initialConfig = dialogController.getInitialConfig();
                this.capacityBar.setValue(initialConfig.getInt(Config.Key.CAPACITY));
                this.stepBar.setValue(initialConfig.getInt(Config.Key.STEP));
                this.hasherMenu.setValue(initialConfig.getHasher(Config.Key.HASHER));
                this.scanMethodMenu.setValue(initialConfig.getScanMethod(Config.Key.SCAN_METHOD));
                this.hashTableController.init(initialConfig, initialData);
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

        radioAnimation.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            hashTableController.setAnimationsEnabled(newValue);
            radioAnimation.setText(newValue ? "Animazioni abilitate" : "Animazioni disabilitate");
        } ));

        hashTableController.getErrors().addListener((ListChangeListener<? super ErrorCodes>) change -> {
            if (change.getList().isEmpty())
                errorMessages.setText("");

            while (change.next()) {
                for (ErrorCodes code : change.getAddedSubList()) {
                    String text = errorMessages.getText();
                    errorMessages.setText((text.isEmpty() ? "" : text + "\n") +  "> " + code);
                    errorMessages.setStyle("-fx-text-fill: red;");
                }
            }
        });

        scanMethodMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldMethod, newMethod) -> {
            stepFieldContainer.setVisible(newMethod == ScanMethod.LINEAR || newMethod == ScanMethod.QUADRATIC);
            secondHasherContainer.setVisible(newMethod == ScanMethod.DOUBLE_HASHING);
            additionalParams.setVisible(newMethod != ScanMethod.RANDOM);
        });
    }

    private final Image playIcon = new Image(getClass().getResourceAsStream("/algat/resources/icons/play.png"));
    private final Image pauseIcon = new Image(getClass().getResourceAsStream("/algat/resources/icons/pause.png"));
    private final Image stopIcon = new Image(getClass().getResourceAsStream("/algat/resources/icons/stop.png"));

    // ==================================
    // === LISTENERS ====================
    // ==================================
    public void insertButtonPressed(ActionEvent event) {
        if (this.hasToSkipAction())
            return;

        ActionDialog dialog = new ActionDialog(
                "Insert Dialog",
                "Insert a new entry into the table",
                true
        );

        dialog.showAndWait().ifPresent(result -> {
            hashTableController.insert(result.getKey(), result.getValue());
        });
    }

    public void removeButtonPressed(ActionEvent event) {
        if (this.hasToSkipAction())
            return;

        ActionDialog dialog = new ActionDialog(
                "Remove Dialog",
                "Remove an entry from the table"
        );

        dialog.showAndWait().ifPresent(result -> hashTableController.remove(result.getKey()));
    }

    public void lessonButtonPressed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/Lessons.fxml"));
            Parent lessonContent = loader.load();
            LessonsController controller = loader.getController();
            controller.setStage(stage);
            stage.getScene().setRoot(lessonContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lockButtonPressed(ActionEvent event) {
        boolean locked = lockButton.getText().equals("Lock");
        lockButton.setText(locked ? "Unlock" : "Lock");

        configSideBar.getChildren().forEach(node -> {
            if (node != lockButton)
                node.setDisable(locked);
        });
    }

    private boolean hasToSkipAction() {
        lockButton.fire();
        Config config = createConfig();

        if (config != null) {
            hashTableController.setConfig(config);
            return false;
        } else {
            lockButton.fire();
            return true;
        }
    }

    private Config createConfig() {
        int capacity = (int)capacityBar.getValue();
        int step = (int)stepBar.getValue();

        Config config = new Config(capacity, hasherMenu.getValue(), scanMethodMenu.getValue());
        config.set(Config.Key.STEP, step);
        config.set(Config.Key.SECOND_HASHER, secondHasherMenu.getValue());

        return config;
    }

    public void playButtonPressed(ActionEvent event) {
        ProbeAnimation animation = hashTableController.getAnimation();
        Animation.Status status = animation.getStatus();

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

    public void setStage (Stage stage) {
        this.stage = stage;
    }

    public void trashButtonPressed(ActionEvent event) {
        hashTableController.clearErrors();
    }

}
