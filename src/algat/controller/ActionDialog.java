package algat.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.io.IOException;

public class ActionDialog extends Dialog<Pair<String, String>> {
    private StringProperty key = new SimpleStringProperty();
    private StringProperty value = new SimpleStringProperty();

    @FXML private TextField keyField;
    @FXML private Text keyError;
    @FXML private VBox valueBox;
    @FXML private TextField valueField;
    @FXML private Text valueError;

    ActionDialog(String title, String headerText, boolean isInsertDialog) {
        this.setTitle(title);
        this.setHeaderText(headerText);

        this.createDialogPane(isInsertDialog);

        this.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK)
                return new Pair<>(getKey(), getValue());

            return null;
        });
    }

    ActionDialog(String title, String headerText) {
        this(title, headerText, false);
    }

    public String getKey() {
        return key.get();
    }

    public String getValue() {
        return value.get();
    }

    private void createDialogPane(boolean isInsertDialog) {
        DialogPane dialogPane = this.getDialogPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/ActionDialog.fxml"));
        loader.setController(this);

        try {
            VBox content = loader.load();
            dialogPane.setContent(content);

            key.bind(keyField.textProperty());

            if (isInsertDialog)
                value.bind(valueField.textProperty());

            valueBox.managedProperty().bind(valueBox.visibleProperty());
            valueBox.setVisible(isInsertDialog);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialogPane.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        Button button = (Button) dialogPane.lookupButton(ButtonType.OK);
        button.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateFields())
                event.consume();
        });
    }

    private boolean validateFields() {
        String key = getKey();
        String value = getValue();

        boolean isKeyEmpty = key.isEmpty();
        keyError.setVisible(isKeyEmpty);

        boolean isEmptyValue = value != null && value.isEmpty();
        valueError.setVisible(isEmptyValue);

        return !isKeyEmpty && !isEmptyValue;
    }
}
