package algat.controller;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class TableNode extends GridPane {
    @FXML private Text key;
    @FXML private Text value;

    TableNode(@NamedArg("key") String key, @NamedArg("value") String value) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/algat/view/TableNode.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            this.key.setText(key);
            this.value.setText(value);
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
