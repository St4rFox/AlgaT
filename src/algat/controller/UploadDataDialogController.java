package algat.controller;

import algat.model.Bucket;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UploadDataDialogController implements Initializable {
    @FXML private TextField selectedFilePath;
    @FXML private Button folderButton;
    @FXML private TableView<Bucket> tableView;
    @FXML private TableColumn<Bucket, String> keyColumn;
    @FXML private TableColumn<Bucket, String> valueColumn;

    private OnConfigCompleted callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.setOnKeyPressed(keyEvent -> {
            Bucket selected = tableView.getSelectionModel().getSelectedItem();

            if (keyEvent.getCode() == KeyCode.BACK_SPACE && selected != null) {
                tableView.getItems().remove(selected);
            }
        });

        keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        keyColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setKey(e.getNewValue()));

        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setValue(e.getNewValue()));
    }

    public void finishButtonPressed(ActionEvent event) {
        ArrayList<Bucket> buckets = new ArrayList<>(tableView.getItems());
        this.callback.configCompleted(buckets);
    }

    public void folderButtonPressed(ActionEvent event) {
        Stage stage = (Stage) folderButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Data File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            selectedFilePath.setText(file.getAbsolutePath());
            LinkedList<Bucket> buckets = getDataFromFile(file);

            if (buckets != null) {
                ObservableList<Bucket> items = tableView.getItems();
                items.addAll(buckets);
            }
        }
    }

    void onConfigCompleted(OnConfigCompleted callback) {
        this.callback = callback;
    }

    private LinkedList<Bucket> getDataFromFile(File file) {
        LinkedList<Bucket> buckets = null;
        try {
            Stream<String> fileStream = Files.lines(file.toPath());
            buckets = fileStream
                    .map(line -> {
                        String[] parts = line.split("\\|");
                        return new Bucket(parts[0], parts[1]);
                    })
                    .collect(Collectors.toCollection(LinkedList::new));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buckets;
    }
}
