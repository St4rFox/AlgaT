package algat.controller;

import algat.Config;
import algat.hashtable.HashTable;
import algat.hashtable.Hasher;
import algat.model.Record;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewerController implements Initializable {
    @FXML private GridPane tableContainer;

    private HashTable table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColumnConstraints keyColumn = new ColumnConstraints();
        keyColumn.setMinWidth(10);
        keyColumn.setPrefWidth(Control.USE_COMPUTED_SIZE);
        ColumnConstraints valueColumn = new ColumnConstraints();
        valueColumn.setMinWidth(10);
        valueColumn.setPercentWidth(Control.USE_COMPUTED_SIZE);
        tableContainer.getColumnConstraints().addAll(keyColumn, valueColumn);
    }

    void initTable(List<Record> data) {
        boolean hasData = data != null;

        if (hasData)
            Config.setTableCapacity(data.size());

        int capacity = Config.getTableCapacity();
        Hasher hasher = Config.getHashFunction();

        this.table = new HashTable(capacity, hasher);

        if (hasData)
            this.loadInitialData(data);

        this.table.setViewer(this);
    }

    void render() {
        int index = 0;
        for (HashTable.HashTableNode node : this.table) {
            Label key = new Label(node.getKey());
            key.getStylesheets().add(this.getClass().getResource("/algat/resources/styles/styles.css").toExternalForm());
            key.getStyleClass().add("viewer-cell");

            Label value = new Label(node.getValue());
            value.getStylesheets().add(this.getClass().getResource("/algat/resources/styles/styles.css").toExternalForm());
            value.getStyleClass().add("viewer-cell");

            tableContainer.addRow(index, key, value);
            index++;
        }
    }

    private void loadInitialData(List<Record> data) {
        for (Record record : data) {
            this.table.put(record.getKey(), record.getValue());
        }
    }
}
