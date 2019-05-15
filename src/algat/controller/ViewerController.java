package algat.controller;

import algat.hashtable.HashTable;
import algat.hashtable.Hasher;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ViewerController {
    @FXML private VBox tableContainer;
    private HashTable table;

    public ViewerController() {
        this.table = new HashTable(10, Hasher.NAIVE);
        this.table.setViewer(this);
    }

    void onInsert(String key, String value) {
        this.table.put(key, value);
    }

    void onRemove(String key) {

    }

    void onFind(String key) {

    }
}
