package algat.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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

        VBox keyBox = new VBox();

        Label keyLabel = new Label("Key");
        TextField keyField = new TextField();

        Text keyError = new Text("Chiave non inserita!");
        keyError.setFill(Color.RED);
        keyError.setVisible(false);
        keyError.setId("keyError");
        keyBox.getChildren().addAll(keyField, keyError);

        keyField.setPromptText("Enter key");
        this.key.bind(keyField.textProperty());
        content.addRow(0, keyLabel, keyBox);


        if (isInsertDialog) {
            VBox valueBox = new VBox();
            Label valueLabel = new Label("Value");
            TextField valueField = new TextField();
            Text valueError = new Text("Valore non inserita!");
            valueError.setFill(Color.RED);
            valueError.setVisible(false);
            valueError.setId("valueError");
            valueField.setPromptText("Enter value");
            this.value.bind(valueField.textProperty());
            valueBox.getChildren().addAll(valueField, valueError);
            content.addRow(1, valueLabel, valueBox);

        }

        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        Button button = (Button) dialogPane.lookupButton(ButtonType.OK);
        button.addEventFilter(ActionEvent.ACTION, event -> {
            if(!validate())
                event.consume();
        });
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

    private boolean validate() {
        String key = getKey();
        String value = getValue();

        boolean flag = true;

        GridPane content = (GridPane) getDialogPane().getContent();
        VBox keyBox = (VBox) getNodeFromGridPane(content, 1, 0);
        VBox valueBox = (VBox) getNodeFromGridPane(content, 1, 1);

        if(key.isEmpty()) {
            Text keyError = (Text)keyBox.getChildren().get(1);
            keyError.setVisible(true);
            flag = false;
        }
        if(value != null && value.isEmpty()) {
            Text valueError = (Text)valueBox.getChildren().get(1);
            valueError.setVisible(true);
            flag = false;
        }
        return flag;
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
