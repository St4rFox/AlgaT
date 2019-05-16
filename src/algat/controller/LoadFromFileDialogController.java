package algat.controller;

import algat.model.Record;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadFromFileDialogController implements Initializable {
    @FXML private TextField selectedFilePath;
    @FXML private Button folderButton;
    @FXML private TableView<Record> tableView;
    @FXML private TableColumn<Record, String> keyColumn;
    @FXML private TableColumn<Record, String> valueColumn;
    private SampleDataDialogController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        keyColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setKey(e.getNewValue()));

        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setValue(e.getNewValue()));
    }

    public void finishButtonPressed(ActionEvent event) {
        List<Record> records = tableView.getItems();
        parentController.closeStage(records);
    }

    public void folderButtonPressed(ActionEvent event) {
        Stage stage = (Stage) folderButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Data File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            selectedFilePath.setText(file.getAbsolutePath());
            LinkedList<Record> records = getDataFromFile(file);

            if (records != null) {
                ObservableList<Record> items = tableView.getItems();
                items.addAll(records);
            }
        }
    }

    private LinkedList<Record> getDataFromFile(File file) {
        LinkedList<Record> records = null;
        try {
            Stream<String> fileStream = Files.lines(file.toPath());
            records = fileStream
                    .map(line -> {
                        String[] parts = line.split("\\|");
                        return new Record(parts[0], parts[1]);
                    })
                    .collect(Collectors.toCollection(LinkedList::new));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    void setParentController(SampleDataDialogController parentController) {
        this.parentController = parentController;
    }
}
