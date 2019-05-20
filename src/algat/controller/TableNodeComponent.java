package algat.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class TableNodeComponent extends GridPane {
    @FXML private Label key;
    @FXML private Label value;

    TableNodeComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/algat/view/TableNodeComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getKey() {
        return this.key.getText();
    }

    public void setKey(String key) {
        this.key.setText(key);
    }

    public String getValue() {
        return this.value.getText();
    }

    public void setValue(String value) {
        this.value.setText(value);
    }
}
