package algat.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class ActionDialog extends Dialog<Pair<String, String>> {
    private StringProperty key = new SimpleStringProperty();
    private StringProperty value = new SimpleStringProperty();

    ActionDialog(String title, String headerText, boolean isInsertDialog) {
        this.setTitle(title);
        this.setHeaderText(headerText);

        GridPane content = new GridPane();
        content.setHgap(10);
        content.setVgap(10);
        content.setPadding(new Insets(10));

        Label keyLabel = new Label("Key");
        TextField keyField = new TextField();
        keyField.setPromptText("Enter key");
        this.key.bind(keyField.textProperty());
        content.addRow(0, keyLabel, keyField);


        if (isInsertDialog) {
            Label valueLabel = new Label("Value");
            TextField valueField = new TextField();
            valueField.setPromptText("Enter value");
            this.value.bind(valueField.textProperty());
            content.addRow(1, valueLabel, valueField);
        }

        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        dialogPane.setContent(content);

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
}
