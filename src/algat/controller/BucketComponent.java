package algat.controller;

import algat.model.Bucket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class BucketComponent extends AnchorPane {
    @FXML private Text key;
    @FXML private Text value;
    @FXML private Text deleted;

    private final Bucket bucket;

    BucketComponent(Bucket bucket) {
        this.bucket = bucket;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/Bucket.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
            this.bindModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindModel() {
        key.textProperty().bind(bucket.keyProperty());
        value.textProperty().bind(bucket.valueProperty());
        deleted.textProperty().bind(bucket.deletedProperty().asString());
    }
}
